package com.dypcoe.qsdta.controller.application;

import com.dypcoe.qsdta.dto.LoginRequest;
import com.dypcoe.qsdta.exception.dao.AuthUserException;
import com.dypcoe.qsdta.exception.dao.ConnectionDataException;
import com.dypcoe.qsdta.exception.dao.ConnectionKeyException;
import com.dypcoe.qsdta.exception.dao.KeyDataAssociationException;
import com.dypcoe.qsdta.exception.dao.UserConnectionException;
import com.dypcoe.qsdta.model.AuthUser;
import com.dypcoe.qsdta.model.QsdtaUser;
import com.dypcoe.qsdta.model.UserConnection;
import com.dypcoe.qsdta.service.ConnectionDataService;
import com.dypcoe.qsdta.service.ConnectionKeyService;
import com.dypcoe.qsdta.service.KeyDataAssociationService;
import com.dypcoe.qsdta.service.UserConnectionService;
import com.dypcoe.qsdta.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/qsdta/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private KeyDataAssociationService keyDataAssociationService;
    @Autowired
    private ConnectionDataService connectionDataService;
    @Autowired
    private ConnectionKeyService connectionKeyService;
    @Autowired
    private UserConnectionService userConnectionService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthUser authUser) {
        try {
            userService.registerUser(authUser);
            return ResponseEntity.ok("Registered Successfully");
        } catch(AuthUserException e) {
            logger.error("Error registering user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            AuthUser loggedInUser = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(loggedInUser);
        } catch(AuthUserException e) {
            logger.error("Error logging in user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody AuthUser user) {
        try {
            AuthUser updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (AuthUserException e) {
            logger.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getUser(@PathVariable String uuid) {
        try {
            QsdtaUser user = userService.getUser(uuid);
            return ResponseEntity.ok(user);
        } catch (AuthUserException e) {
            logger.error("Error fetching user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/page")
    public ResponseEntity<?> getUserPage(@RequestParam int page, @RequestParam int size) {
        try {
            List<QsdtaUser> users = userService.getUserPage(page, size);
            return ResponseEntity.ok(users);
        } catch (AuthUserException e) {
            logger.error("Error fetching users page: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteAuthUser(@PathVariable String uuid) {
        try {
            List<UserConnection> userConnections;
            try {
                userConnections = userConnectionService.getUserConnections(userService.getAuthUser(uuid));
                for(UserConnection userConnection : userConnections) {
                    keyDataAssociationService.deleteKeyDataAssociationByConnectionKey(connectionKeyService.getConnectionKey(userConnection));
                    connectionKeyService.deleteConnectionKey(connectionKeyService.getConnectionKey(userConnection).getId());
                    connectionDataService.deleteAllConnectionData(userConnection);
                    userConnectionService.deleteUserConnection(userConnection);
                }
            } catch (UserConnectionException e) {
                logger.info("No connections found for this use: " + uuid);
            } catch (ConnectionKeyException | ConnectionDataException | KeyDataAssociationException e) {
                throw new RuntimeException(e);
            }
            userService.deleteAuthUser(uuid);
            return ResponseEntity.ok("User deleted successfully");
        } catch (AuthUserException e) {
            logger.error("Error deleting user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
