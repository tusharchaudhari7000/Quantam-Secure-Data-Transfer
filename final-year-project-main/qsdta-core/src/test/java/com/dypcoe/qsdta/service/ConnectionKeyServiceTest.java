package com.dypcoe.qsdta.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dypcoe.qsdta.dao.ConnectionKeyRepo;
import com.dypcoe.qsdta.exception.dao.ConnectionKeyException;
import com.dypcoe.qsdta.model.ConnectionKey;
import com.dypcoe.qsdta.model.UserConnection;

@ExtendWith(MockitoExtension.class)
class ConnectionKeyServiceTest {

    @Mock
    private ConnectionKeyRepo connectionKeyRepo;

    @InjectMocks
    private ConnectionKeyService connectionKeyService;

    private ConnectionKey connectionKey;
    private UserConnection userConnection;

    @BeforeEach
    void setUp() {
        userConnection = new UserConnection();
        userConnection.setId(1);

        connectionKey = new ConnectionKey();
        connectionKey.setId(1);
        connectionKey.setUserConnection(userConnection);
        connectionKey.setExpiryTimestamp(LocalDateTime.now().plusDays(10));
    }

    @Test
    void testInsertConnectionKey() throws ConnectionKeyException {
        when(connectionKeyRepo.save(connectionKey)).thenReturn(connectionKey);
        ConnectionKey result = connectionKeyService.insertConnectionKey(connectionKey);
        assertNotNull(result);
        assertEquals(connectionKey, result);
    }

    @Test
    void testInsertConnectionKeyThrowsException() {
        when(connectionKeyRepo.save(any())).thenThrow(new IllegalArgumentException());
        assertThrows(ConnectionKeyException.class, () -> connectionKeyService.insertConnectionKey(connectionKey));
    }

    @Test
    void testGetConnectionKey() throws ConnectionKeyException {
        when(connectionKeyRepo.existsByUserConnection(userConnection)).thenReturn(true);
        when(connectionKeyRepo.findByUserConnection(userConnection)).thenReturn(connectionKey);
        ConnectionKey result = connectionKeyService.getConnectionKey(userConnection);
        assertNotNull(result);
        assertEquals(connectionKey, result);
    }

    @Test
    void testGetConnectionKeyNotFound() {
        when(connectionKeyRepo.existsByUserConnection(userConnection)).thenReturn(false);
        assertThrows(ConnectionKeyException.class, () -> connectionKeyService.getConnectionKey(userConnection));
    }

    @Test
    void testDeleteConnectionKey() {
        assertDoesNotThrow(() -> connectionKeyService.deleteConnectionKey(1));
    }

    @Test
    void testDeleteConnectionKeyThrowsException() {
        doThrow(new IllegalArgumentException()).when(connectionKeyRepo).deleteById(1);
        assertThrows(ConnectionKeyException.class, () -> connectionKeyService.deleteConnectionKey(1));
    }

    @Test
    void testUpdateConnectionKeyExpiry() throws ConnectionKeyException {
        when(connectionKeyRepo.findById(1)).thenReturn(Optional.of(connectionKey));
        when(connectionKeyRepo.save(any())).thenReturn(connectionKey);

        ConnectionKey result = connectionKeyService.updateConnectionKeyExpiry(1);
        assertNotNull(result);
    }

    @Test
    void testUpdateConnectionKeyExpiryNotFound() {
        when(connectionKeyRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(ConnectionKeyException.class, () -> connectionKeyService.updateConnectionKeyExpiry(1));
    }

    @Test
    void testUpdateConnectionKeyExpiryThrowsException() {
        when(connectionKeyRepo.findById(1)).thenThrow(new RuntimeException());
        assertThrows(ConnectionKeyException.class, () -> connectionKeyService.updateConnectionKeyExpiry(1));
    }
}
