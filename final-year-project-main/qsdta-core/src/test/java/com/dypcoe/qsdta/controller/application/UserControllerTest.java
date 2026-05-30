package com.dypcoe.qsdta.controller.application;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dypcoe.qsdta.exception.dao.*;
import com.dypcoe.qsdta.model.*;
import com.dypcoe.qsdta.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private KeyDataAssociationService keyDataAssociationService;
    @Mock
    private ConnectionDataService connectionDataService;
    @Mock
    private ConnectionKeyService connectionKeyService;
    @Mock
    private UserConnectionService userConnectionService;

    private AuthUser authUser;
    private QsdtaUser qsdtaUser;
    private UserConnection userConnection;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser();
        qsdtaUser = new QsdtaUser();
        userConnection = new UserConnection();
    }

    @Test
    void testUpdateUser_Success() throws AuthUserException {
        when(userService.updateUser(any(AuthUser.class))).thenReturn(authUser);
        ResponseEntity<?> response = userController.updateUser(authUser);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(authUser, response.getBody());
    }

    @Test
    void testUpdateUser_Failure() throws AuthUserException {
        doThrow(new AuthUserException("Update failed",null)).when(userService).updateUser(any(AuthUser.class));
        ResponseEntity<?> response = userController.updateUser(authUser);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Update failed", response.getBody());
    }

    @Test
    void testGetUser_Success() throws AuthUserException {
        when(userService.getUser(anyString())).thenReturn(qsdtaUser);
        ResponseEntity<?> response = userController.getUser("123");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(qsdtaUser, response.getBody());
    }

    @Test
    void testGetUser_Failure() throws AuthUserException {
        doThrow(new AuthUserException("User not found",null)).when(userService).getUser(anyString());
        ResponseEntity<?> response = userController.getUser("123");
        assertEquals(400, response.getStatusCode().value());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testGetUserPage_Success() throws AuthUserException {
        List<QsdtaUser> users = List.of(qsdtaUser);
        when(userService.getUserPage(anyInt(), anyInt())).thenReturn(users);
        ResponseEntity<?> response = userController.getUserPage(1, 5);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(users, response.getBody());
    }

    @Test
    void testGetUserPage_Failure() throws AuthUserException {
        doThrow(new AuthUserException("Error fetching users page",null)).when(userService).getUserPage(anyInt(), anyInt());
        ResponseEntity<?> response = userController.getUserPage(1, 5);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Error fetching users page", response.getBody());
    }

    @Test
    void testDeleteAuthUser_Success() throws Exception {
        when(userService.getAuthUser(anyString())).thenReturn(authUser);
        when(userConnectionService.getUserConnections(authUser)).thenReturn(List.of(userConnection));
        when(connectionKeyService.getConnectionKey(userConnection)).thenReturn(new ConnectionKey());

        ResponseEntity<?> response = userController.deleteAuthUser("123");
        assertEquals(200, response.getStatusCode().value());
        assertEquals("User deleted successfully", response.getBody());
    }

    @Test
    void testDeleteAuthUser_NoConnections() throws Exception {
        when(userService.getAuthUser(anyString())).thenReturn(authUser);
        when(userConnectionService.getUserConnections(authUser)).thenThrow(new UserConnectionException("No connections found",null));
        ResponseEntity<?> response = userController.deleteAuthUser("123");
        assertEquals(200, response.getStatusCode().value());
        assertEquals("User deleted successfully", response.getBody());
    }

    @Test
    void testDeleteAuthUser_Failure() throws Exception {
        doThrow(new AuthUserException("Delete failed",null))
                .when(userService).deleteAuthUser(anyString());
        ResponseEntity<?> response = userController.deleteAuthUser("123");
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Delete failed", response.getBody());
    }
}
