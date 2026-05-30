package com.dypcoe.qsdta.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dypcoe.qsdta.dao.KeyDataAssociationRepo;
import com.dypcoe.qsdta.exception.dao.KeyDataAssociationException;
import com.dypcoe.qsdta.model.ConnectionKey;
import com.dypcoe.qsdta.model.KeyDataAssociation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KeyDataAssociationServiceTest {

    @Mock
    private KeyDataAssociationRepo keyDataAssociationRepo;

    @InjectMocks
    private KeyDataAssociationService keyDataAssociationService;

    private KeyDataAssociation keyDataAssociation;
    private ConnectionKey connectionKey;

    @BeforeEach
    void setUp() {
        keyDataAssociation = new KeyDataAssociation();
        connectionKey = new ConnectionKey();
        connectionKey.setId(-1);
    }

    @Test
    void testInserKeyDataAssociation_Success() throws KeyDataAssociationException {
        when(keyDataAssociationRepo.save(keyDataAssociation)).thenReturn(keyDataAssociation);

        KeyDataAssociation result = keyDataAssociationService.inserKeyDataAssociation(keyDataAssociation);
        
        assertNotNull(result);
        assertEquals(keyDataAssociation, result);
        verify(keyDataAssociationRepo, times(1)).save(keyDataAssociation);
    }

    @Test
    void testInserKeyDataAssociation_Failure() {
        when(keyDataAssociationRepo.save(keyDataAssociation)).thenThrow(new IllegalArgumentException("Invalid data"));

        KeyDataAssociationException exception = assertThrows(KeyDataAssociationException.class, 
            () -> keyDataAssociationService.inserKeyDataAssociation(keyDataAssociation));
        
        assertEquals("KeyDataAssociated Insertion Failed.", exception.getMessage());
        verify(keyDataAssociationRepo, times(1)).save(keyDataAssociation);
    }

    @Test
    void testDeleteKeyDataAssociationByConnectionKey_Success() throws KeyDataAssociationException {
        doNothing().when(keyDataAssociationRepo).deleteByConnectionKey(connectionKey);

        assertDoesNotThrow(() -> keyDataAssociationService.deleteKeyDataAssociationByConnectionKey(connectionKey));
        verify(keyDataAssociationRepo, times(1)).deleteByConnectionKey(connectionKey);
    }

    @Test
    void testDeleteKeyDataAssociationByConnectionKey_Failure() {
        doThrow(new IllegalArgumentException("Invalid key ID"))
            .when(keyDataAssociationRepo).deleteByConnectionKey(connectionKey);

        KeyDataAssociationException exception = assertThrows(KeyDataAssociationException.class, 
            () -> keyDataAssociationService.deleteKeyDataAssociationByConnectionKey(connectionKey));
        
        assertEquals("Specified key_id not found. Key ID: -1", exception.getMessage());
        verify(keyDataAssociationRepo, times(1)).deleteByConnectionKey(connectionKey);
    }
}
