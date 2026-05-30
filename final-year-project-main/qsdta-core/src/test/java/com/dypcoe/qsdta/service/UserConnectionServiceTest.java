package com.dypcoe.qsdta.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dypcoe.qsdta.dao.UserConnectionRepo;
import com.dypcoe.qsdta.exception.dao.UserConnectionException;
import com.dypcoe.qsdta.model.AuthUser;
import com.dypcoe.qsdta.model.UserConnection;

@ExtendWith(MockitoExtension.class)
class UserConnectionServiceTest {

    @InjectMocks
    private UserConnectionService userConnectionService;

    @Mock
    private UserConnectionRepo userConnectionRepo;

    private UserConnection userConnection;
    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser();
        authUser.setEmail("test@example.com");

        userConnection = new UserConnection();
        userConnection.setId(1);
        userConnection.setAuthUser1(authUser);
    }

    @Test
    void testInsertUserConnection_Success() throws UserConnectionException {
        when(userConnectionRepo.save(userConnection)).thenReturn(userConnection);
        UserConnection result = userConnectionService.insertUserConnection(userConnection);
        assertNotNull(result);
        assertEquals(userConnection, result);
    }

    @Test
    void testInsertUserConnection_Failure() {
        when(userConnectionRepo.save(any())).thenThrow(IllegalArgumentException.class);
        assertThrows(UserConnectionException.class, () -> userConnectionService.insertUserConnection(userConnection));
    }

    @Test
    void testGetUserConnections_Success() throws UserConnectionException {
        when(userConnectionRepo.existsByAuthUser1OrAuthUser2(authUser, authUser)).thenReturn(true);
        when(userConnectionRepo.findAllByAuthUser1OrAuthUser2(authUser, authUser)).thenReturn(Arrays.asList(userConnection));

        List<UserConnection> result = userConnectionService.getUserConnections(authUser);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetUserConnections_NotFound() {
        when(userConnectionRepo.existsByAuthUser1OrAuthUser2(authUser, authUser)).thenReturn(false);
        assertThrows(UserConnectionException.class, () -> userConnectionService.getUserConnections(authUser));
    }

    @Test
    void testGetUserConnections_Failure() {
        when(userConnectionRepo.existsByAuthUser1OrAuthUser2(any(), any())).thenThrow(IllegalArgumentException.class);
        assertThrows(UserConnectionException.class, () -> userConnectionService.getUserConnections(authUser));
    }

    @Test
    void testDeleteUserConnection_Success() throws UserConnectionException {
        when(userConnectionRepo.existsById(userConnection.getId())).thenReturn(true);
        doNothing().when(userConnectionRepo).delete(userConnection);

        assertDoesNotThrow(() -> userConnectionService.deleteUserConnection(userConnection));
    }

    @Test
    void testDeleteUserConnection_NotFound() {
        when(userConnectionRepo.existsById(userConnection.getId())).thenReturn(false);
        assertThrows(UserConnectionException.class, () -> userConnectionService.deleteUserConnection(userConnection));
    }

    @Test
    void testDeleteUserConnection_Failure() {
        when(userConnectionRepo.existsById(any())).thenThrow(IllegalArgumentException.class);
        assertThrows(UserConnectionException.class, () -> userConnectionService.deleteUserConnection(userConnection));
    }
}
