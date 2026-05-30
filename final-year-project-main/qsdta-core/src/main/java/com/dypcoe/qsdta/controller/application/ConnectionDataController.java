package com.dypcoe.qsdta.controller.application;

import com.dypcoe.qsdta.exception.dao.ConnectionDataException;
import com.dypcoe.qsdta.exception.dao.ConnectionKeyException;
import com.dypcoe.qsdta.exception.dao.KeyDataAssociationException;
import com.dypcoe.qsdta.exception.simulation.CryptoUtilityException;
import com.dypcoe.qsdta.model.ConnectionData;
import com.dypcoe.qsdta.model.KeyDataAssociation;
import com.dypcoe.qsdta.model.UserConnection;
import com.dypcoe.qsdta.service.ConnectionDataService;
import com.dypcoe.qsdta.service.ConnectionKeyService;
import com.dypcoe.qsdta.service.KeyDataAssociationService;
import com.dypcoe.qsdta.simulation.utility.CryptoUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/qsdta/api/connection-data")
public class ConnectionDataController {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionDataController.class);
    /**
     * Below Crypto Util is used to encrypt and decrypt the data must remove from the server
     * after migrating all the utilities of simulation to the client module.
     */
    private static final CryptoUtil CRYPTO_UTIL = CryptoUtil.getInstance();

    @Autowired
    private ConnectionDataService connectionDataService;
    @Autowired
    private ConnectionKeyService connectionKeyService;
    @Autowired
    private KeyDataAssociationService keyDataAssociationService;

    @PostMapping("/add")
    public ResponseEntity<?> insertConnectionData(@RequestBody ConnectionData connectionData) {
        try {
            connectionData.setByteData(CRYPTO_UTIL.encrypt(
                connectionKeyService.getConnectionKey(connectionData.getUserConnection()).getCryptoKey(),
                connectionData.getByteData()
            ));
            ConnectionData savedData = connectionDataService.insertConnectionData(connectionData);
            KeyDataAssociation keyDataAssociation = new KeyDataAssociation();
            keyDataAssociation.setConnectionData(connectionData);
            keyDataAssociation.setConnectionKey(connectionKeyService.getConnectionKey(
                connectionData.getUserConnection()
            ));
            keyDataAssociationService.inserKeyDataAssociation(keyDataAssociation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedData);
        } catch (ConnectionDataException | CryptoUtilityException | ConnectionKeyException | KeyDataAssociationException e) {
            logger.error("Error adding connection data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/all")
    public ResponseEntity<?> getConnectionDatas(@RequestBody UserConnection userConnection) {
        try {
            List<ConnectionData> connectionDataList = connectionDataService.getConnectionDatas(userConnection);
            for(ConnectionData connectionData : connectionDataList) {
                connectionData.setByteData(CRYPTO_UTIL.decrypt(
                    connectionKeyService.getConnectionKey(userConnection).getCryptoKey(),
                    connectionData.getByteData()
                ));
            }
            return ResponseEntity.ok(connectionDataList);
        } catch (ConnectionDataException | CryptoUtilityException | ConnectionKeyException e) {
            logger.error("Error retrieving connection data", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/by-type")
    public ResponseEntity<?> getConnectionDatasByDataType(@RequestBody UserConnection userConnection, @RequestParam String dataType) {
        try {
            List<ConnectionData> connectionDataList = connectionDataService.getConnectionDatasByDataType(userConnection, dataType);
            for(ConnectionData connectionData : connectionDataList) {
                connectionData.setByteData(CRYPTO_UTIL.decrypt(
                    connectionKeyService.getConnectionKey(userConnection).getCryptoKey(),
                    connectionData.getByteData()
                ));
            }
            return ResponseEntity.ok(connectionDataList);
        } catch (ConnectionDataException | CryptoUtilityException | ConnectionKeyException e) {
            logger.error("Error retrieving connection data by type", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateConnectionData(@RequestBody ConnectionData connectionData) {
        try {
            connectionData.setByteData(CRYPTO_UTIL.encrypt(
                connectionKeyService.getConnectionKey(connectionData.getUserConnection()).getCryptoKey(),
                connectionData.getByteData()
            ));
            ConnectionData updatedData = connectionDataService.updateConnectionData(connectionData);
            return ResponseEntity.ok(updatedData);
        } catch (ConnectionDataException | CryptoUtilityException | ConnectionKeyException e) {
            logger.error("Error updating connection data", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteConnectionData(@RequestBody ConnectionData connectionData) {
        try {
            keyDataAssociationService.deleteByConnectionData(connectionData);
            connectionDataService.deleteConnectionData(connectionData.getId());
            return ResponseEntity.ok("Connection data deleted successfully");
        } catch (KeyDataAssociationException | ConnectionDataException e) {
            logger.error("Error deleting connection data", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAllConnectionData(@RequestBody UserConnection userConnection) {
        try {
            keyDataAssociationService.deleteAllKeyDataAssociation(connectionKeyService.getConnectionKey(userConnection));
            connectionDataService.deleteAllConnectionData(userConnection);
            return ResponseEntity.ok("All connection data deleted successfully");
        } catch (ConnectionDataException | KeyDataAssociationException | ConnectionKeyException e) {
            logger.error("Error deleting all connection data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
