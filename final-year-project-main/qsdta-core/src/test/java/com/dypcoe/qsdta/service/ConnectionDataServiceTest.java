package com.dypcoe.qsdta.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dypcoe.qsdta.dao.ConnectionDataRepo;
import com.dypcoe.qsdta.exception.dao.ConnectionDataException;
import com.dypcoe.qsdta.model.ConnectionData;
import com.dypcoe.qsdta.model.UserConnection;

@ExtendWith(MockitoExtension.class)
class ConnectionDataServiceTest {

    @Mock
    private ConnectionDataRepo connectionDataRepo;

    @InjectMocks
    private ConnectionDataService connectionDataService;

    private ConnectionData connectionData;
    private UserConnection userConnection;

    @BeforeEach
    void setUp() {
        userConnection = new UserConnection();
        userConnection.setId(1);
        
        connectionData = new ConnectionData();
        connectionData.setId(1);
        connectionData.setUserConnection(userConnection);
        connectionData.setDataType("TestType");
        connectionData.setOwner("TestOwner");
    }
    
    @Test
    void testInsertConnectionData_Success() throws ConnectionDataException {
        when(connectionDataRepo.save(connectionData)).thenReturn(connectionData);
        ConnectionData result = connectionDataService.insertConnectionData(connectionData);
        assertEquals(connectionData, result);
    }

    @Test
    void testInsertConnectionData_Exception() {
        when(connectionDataRepo.save(any())).thenThrow(new IllegalArgumentException());
        assertThrows(ConnectionDataException.class, () -> connectionDataService.insertConnectionData(connectionData));
    }

    @Test
    void testGetConnectionDatas_Success() throws ConnectionDataException {
        when(connectionDataRepo.existsByUserConnection(userConnection)).thenReturn(true);
        when(connectionDataRepo.findAllByUserConnection(userConnection)).thenReturn(Arrays.asList(connectionData));

        List<ConnectionData> result = connectionDataService.getConnectionDatas(userConnection);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetConnectionDatas_NotFound() {
        when(connectionDataRepo.existsByUserConnection(userConnection)).thenReturn(false);
        assertThrows(ConnectionDataException.class, () -> connectionDataService.getConnectionDatas(userConnection));
    }

    @Test
    void testGetConnectionDatasByDataType_Success() throws ConnectionDataException {
        when(connectionDataRepo.existsByUserConnection(userConnection)).thenReturn(true);
        when(connectionDataRepo.findAllByUserConnectionAndDataType(userConnection, "TestType"))
                .thenReturn(Arrays.asList(connectionData));
        List<ConnectionData> result = connectionDataService.getConnectionDatasByDataType(userConnection, "TestType");
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetConnectionDatasByDataType_NotFound() {
        when(connectionDataRepo.existsByUserConnection(userConnection)).thenReturn(false);
        assertThrows(ConnectionDataException.class, () -> connectionDataService.getConnectionDatasByDataType(userConnection, "TestType"));
    }

    @Test
    void testDeleteConnectionData_Success() throws ConnectionDataException {
        when(connectionDataRepo.existsById(1)).thenReturn(true);
        doNothing().when(connectionDataRepo).deleteById(1);
        assertDoesNotThrow(() -> connectionDataService.deleteConnectionData(1));
    }

    @Test
    void testDeleteConnectionData_NotFound() {
        when(connectionDataRepo.existsById(1)).thenReturn(false);
        assertThrows(ConnectionDataException.class, () -> connectionDataService.deleteConnectionData(1));
    }

    @Test
    void testUpdateConnectionData_Success() throws ConnectionDataException {
        when(connectionDataRepo.findById(1)).thenReturn(Optional.of(connectionData));
        when(connectionDataRepo.save(connectionData)).thenReturn(connectionData);
        ConnectionData result = connectionDataService.updateConnectionData(connectionData);
        assertEquals(connectionData, result);
    }

    @Test
    void testUpdateConnectionData_NotFound() {
        when(connectionDataRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(ConnectionDataException.class, () -> connectionDataService.updateConnectionData(connectionData));
    }
}

