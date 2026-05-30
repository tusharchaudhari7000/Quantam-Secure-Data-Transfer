package com.dypcoe.qsdta.controller.application;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dypcoe.qsdta.exception.dao.UserConnectionException;
import com.dypcoe.qsdta.model.AuthUser;
import com.dypcoe.qsdta.model.ConnectionKey;
import com.dypcoe.qsdta.model.UserConnection;
import com.dypcoe.qsdta.service.ConnectionDataService;
import com.dypcoe.qsdta.service.ConnectionKeyService;
import com.dypcoe.qsdta.service.KeyDataAssociationService;
import com.dypcoe.qsdta.service.UserConnectionService;
import com.dypcoe.qsdta.simulation.QuantumKeyDistribution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserConnectionControllerTest {

    @InjectMocks
    private UserConnectionController userConnectionController;
    @Mock
    private UserConnectionService userConnectionService;
    @Mock
    private KeyDataAssociationService keyDataAssociationService;
    @Mock
    private ConnectionDataService connectionDataService;
    @Mock
    private ConnectionKeyService connectionKeyService;
    @Mock
    private QuantumKeyDistribution quantumKeyDistribution;
    @Mock
    private Logger logger;

    private UserConnection userConnection;
    private AuthUser authUser;
    private ConnectionKey connectionKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userConnection = new UserConnection();
        authUser = new AuthUser();
        connectionKey = new ConnectionKey();
        connectionKey.setUserConnection(userConnection);
        connectionKey.setCryptoKey("test-key".getBytes());
        connectionKey.setCreatedTimestamp(LocalDateTime.now());
        connectionKey.setExpiryTimestamp(LocalDateTime.now().plusDays(30));
    }

    @Test
    void testInsertUserConnection_Success() throws Exception {
        when(userConnectionService.insertUserConnection(any())).thenReturn(userConnection);
        when(connectionKeyService.insertConnectionKey(any())).thenReturn(connectionKey);

        ResponseEntity<?> response = userConnectionController.insertUserConnection(userConnection, 1000);
        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void testInsertUserConnection_InsufficientPhotons() {
        ResponseEntity<?> entity = userConnectionController.insertUserConnection(userConnection, 500);
        assertEquals(500, entity.getStatusCode().value());
    }

    @Test
    void testGetUserConnections_Success() throws UserConnectionException {
        List<UserConnection> connections = Arrays.asList(userConnection);
        when(userConnectionService.getUserConnections(authUser)).thenReturn(connections);

        ResponseEntity<?> response = userConnectionController.getUserConnections(authUser);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(connections, response.getBody());
    }

    @Test
    void testGetUserConnections_Failure() throws UserConnectionException {
        when(userConnectionService.getUserConnections(authUser)).thenThrow(new UserConnectionException("Error fetching",null));
        ResponseEntity<?> response = userConnectionController.getUserConnections(authUser);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testDeleteUserConnection_Success() throws UserConnectionException {
        doNothing().when(userConnectionService).deleteUserConnection(any());
        ResponseEntity<?> response = userConnectionController.deleteUserConnection(userConnection);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testDeleteUserConnection_Failure() throws UserConnectionException {
        doThrow(new UserConnectionException("Error deleting",null))
                .when(userConnectionService).deleteUserConnection(any());
        ResponseEntity<?> response = userConnectionController.deleteUserConnection(userConnection);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testDeleteAllUserConnections_Success() throws Exception {
        when(userConnectionService.getUserConnections(authUser)).thenReturn(Arrays.asList(userConnection));
        when(connectionKeyService.getConnectionKey(userConnection)).thenReturn(connectionKey);
        doNothing().when(keyDataAssociationService).deleteAllKeyDataAssociation(any());
        doNothing().when(connectionKeyService).deleteConnectionKey(anyInt());
        doNothing().when(connectionDataService).deleteAllConnectionData(any());
        doNothing().when(userConnectionService).deleteUserConnection(any());

        ResponseEntity<?> response = userConnectionController.deleteAllUserConnections(authUser);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testDeleteAllUserConnections_Failure() throws Exception {
        when(userConnectionService.getUserConnections(authUser)).thenThrow(new UserConnectionException("Error",null));
        ResponseEntity<?> response = userConnectionController.deleteAllUserConnections(authUser);
        assertEquals(500, response.getStatusCode().value());
    }
}
