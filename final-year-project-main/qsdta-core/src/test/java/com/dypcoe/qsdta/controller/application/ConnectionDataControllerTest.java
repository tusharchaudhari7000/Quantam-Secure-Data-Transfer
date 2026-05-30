package com.dypcoe.qsdta.controller.application;

import com.dypcoe.qsdta.service.ConnectionDataService;
import com.dypcoe.qsdta.service.ConnectionKeyService;
import com.dypcoe.qsdta.service.KeyDataAssociationService;
import com.dypcoe.qsdta.simulation.utility.CryptoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ConnectionDataControllerTest {
    private MockMvc mockMvc;
    @Mock
    private ConnectionDataService connectionDataService;
    @Mock
    private ConnectionKeyService connectionKeyService;
    @Mock
    private KeyDataAssociationService keyDataAssociationService;
    @Mock
    private CryptoUtil cryptoUtil;
    @InjectMocks
    private ConnectionDataController connectionDataController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(connectionDataController).build();
    }

    @Test
    void testGetConnectionDatas() throws Exception {
        when(connectionDataService.getConnectionDatas(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/qsdta/api/connection-data/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetConnectionDatasByDataType() throws Exception {
        when(connectionDataService.getConnectionDatasByDataType(any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/qsdta/api/connection-data/by-type")
                        .param("dataType", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAllConnectionData() throws Exception {
        doNothing().when(keyDataAssociationService).deleteAllKeyDataAssociation(any());
        doNothing().when(connectionDataService).deleteAllConnectionData(any());

        mockMvc.perform(delete("/qsdta/api/connection-data/delete-all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }
}
