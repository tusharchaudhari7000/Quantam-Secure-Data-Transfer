package com.dypcoe.qsdta.controller.application;

import com.dypcoe.qsdta.exception.dao.ConnectionDataException;
import com.dypcoe.qsdta.exception.dao.ConnectionKeyException;
import com.dypcoe.qsdta.exception.dao.KeyDataAssociationException;
import com.dypcoe.qsdta.exception.dao.UserConnectionException;
import com.dypcoe.qsdta.exception.simulation.CryptoUtilityException;
import com.dypcoe.qsdta.exception.simulation.SimulationException;
import com.dypcoe.qsdta.model.AuthUser;
import com.dypcoe.qsdta.model.ConnectionKey;
import com.dypcoe.qsdta.model.UserConnection;
import com.dypcoe.qsdta.service.ConnectionDataService;
import com.dypcoe.qsdta.service.ConnectionKeyService;
import com.dypcoe.qsdta.service.KeyDataAssociationService;
import com.dypcoe.qsdta.service.UserConnectionService;
import com.dypcoe.qsdta.simulation.QuantumKeyDistribution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/qsdta/api/user/connection")
public class UserConnectionController {
    private static final Logger logger = LoggerFactory.getLogger(UserConnectionController.class);
    /*
     * Below QKD instance has to be removed in next update.
     * Update project with the client module and remove this qkd dependecy
     * from the server.
     * QKD can be handled on client side server is only resposible
     * to handle the data in encrypted format.
     * DATE: 23 Feb 2025
     */
    private static final QuantumKeyDistribution QKD = QuantumKeyDistribution.getInstance();

    @Autowired
    private UserConnectionService userConnectionService;
    @Autowired
    private KeyDataAssociationService keyDataAssociationService;
    @Autowired
    private ConnectionDataService connectionDataService;
    @Autowired
    private ConnectionKeyService connectionKeyService;

    @PostMapping("/insert")
    public ResponseEntity<?> insertUserConnection(@RequestBody UserConnection userConnection, @RequestParam int photons) {
        try {
            UserConnection savedConnection = userConnectionService.insertUserConnection(userConnection);
            ConnectionKey connectionKey = new ConnectionKey();
            connectionKey.setUserConnection(userConnection);
            if (photons >= 1000) {
                connectionKey.setCryptoKey(QKD.establishConnectionKey(photons));
            } else {
                throw new RuntimeException("Insufficient photons to establish a secure connection key.");
            }
            connectionKey.setCreatedTimestamp(LocalDateTime.now());
            connectionKey.setExpiryTimestamp(connectionKey.getCreatedTimestamp().plusDays(30));
            connectionKeyService.insertConnectionKey(connectionKey);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedConnection);
        } catch (RuntimeException | UserConnectionException | ConnectionKeyException | SimulationException | CryptoUtilityException e) {
            logger.error("Error adding user connection", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getUserConnections(@RequestParam String uuid) {
        try {
            AuthUser authUser = new AuthUser();
            authUser.setUuid(uuid); // Manually create the object from query param
            List<UserConnection> connections = userConnectionService.getUserConnections(authUser);
            return ResponseEntity.ok(connections);
        } catch (UserConnectionException e) {
            logger.error("Error fetching user connections", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserConnection(@RequestBody UserConnection userConnection) {
        try {
            keyDataAssociationService.deleteKeyDataAssociationByConnectionKey(connectionKeyService.getConnectionKey(userConnection));
            connectionKeyService.deleteConnectionKey(connectionKeyService.getConnectionKey(userConnection).getId());
            connectionDataService.deleteAllConnectionData(userConnection);
            userConnectionService.deleteUserConnection(userConnection);
            return ResponseEntity.ok("User connection deleted successfully");
        } catch (UserConnectionException e) {
            logger.error("Error deleting user connection", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ConnectionKeyException | ConnectionDataException | KeyDataAssociationException e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAllUserConnections(@RequestBody AuthUser authUser) {
        try {
            List<UserConnection> userConnections = userConnectionService.getUserConnections(authUser);
            for(UserConnection userConnection : userConnections) {
                keyDataAssociationService.deleteAllKeyDataAssociation(connectionKeyService.getConnectionKey(userConnection));
                connectionKeyService.deleteConnectionKey(connectionKeyService.getConnectionKey(userConnection).getUserConnection().getId());
                connectionDataService.deleteAllConnectionData(userConnection);
                userConnectionService.deleteUserConnection(userConnection);
            }
            return ResponseEntity.ok("All user connections deleted successfully");
        } catch (UserConnectionException | KeyDataAssociationException | ConnectionKeyException | ConnectionDataException e) {
            logger.error("Error deleting all user connections", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
