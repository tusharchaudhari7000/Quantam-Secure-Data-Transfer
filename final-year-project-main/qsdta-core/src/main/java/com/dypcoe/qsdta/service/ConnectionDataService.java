package com.dypcoe.qsdta.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dypcoe.qsdta.dao.ConnectionDataRepo;
import com.dypcoe.qsdta.exception.dao.ConnectionDataException;
import com.dypcoe.qsdta.model.ConnectionData;
import com.dypcoe.qsdta.model.UserConnection;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConnectionDataService {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionDataService.class);
    @Autowired
    private ConnectionDataRepo connectionDataRepo;

    public ConnectionData insertConnectionData(ConnectionData connectionData) throws ConnectionDataException {
        try {
            return connectionDataRepo.save(connectionData);
        } catch(IllegalArgumentException e) {
            logger.error("Error inserting connection data", e);
            throw new ConnectionDataException("Failed to insert connection data", e);
        }
    }

    public List<ConnectionData> getConnectionDatas(UserConnection userConnection) throws ConnectionDataException {
        try {
            if(connectionDataRepo.existsByUserConnection(userConnection)) {
                return connectionDataRepo.findAllByUserConnection(userConnection);
            } else {
                logger.info("Connection data not found for connection: " + userConnection.getId());
                throw new ConnectionDataException("Connection data not found", new Throwable());
            }
        } catch(IllegalArgumentException e) {
            logger.error("Error retrieving connection data", e);
            throw new ConnectionDataException("Failed to get connection data", e);
        }
    }

    public List<ConnectionData> getConnectionDatasByDataType(UserConnection userConnection, String dataType) throws ConnectionDataException {
        try {
            if(connectionDataRepo.existsByUserConnection(userConnection)) {
                return connectionDataRepo.findAllByUserConnectionAndDataType(userConnection, dataType);
            } else {
                logger.info("Connection data not found for connection: " + userConnection.getId());
                throw new ConnectionDataException("Connection data not found", new Throwable());
            }
        } catch(IllegalArgumentException e) {
            logger.error("Error retrieving connection data", e);
            throw new ConnectionDataException("Failed to get connection data", e);
        }
    }

    public void deleteConnectionData(int connctionDataId) throws ConnectionDataException {
        try {
            if(connectionDataRepo.existsById(Integer.valueOf(connctionDataId))) {
                connectionDataRepo.deleteById(connctionDataId);
            } else {
                logger.info("Data not found for connection data id: " + connctionDataId);
                throw new ConnectionDataException("Data not found to delete", new Throwable());
            }
        } catch(IllegalArgumentException e) {
            logger.error("Error deleting connection data", e);
            throw new ConnectionDataException("Failed to delete connection data", e);
        }
    }

    @Transactional
    public void deleteAllConnectionData(UserConnection userConnection) throws ConnectionDataException {
        try {
            connectionDataRepo.deleteAllByUserConnection(userConnection);
        } catch(IllegalArgumentException e) {
            logger.error("Error deleting connection data", e);
            throw new ConnectionDataException("Failed to delete connection data", e);
        }
    }

    public ConnectionData updateConnectionData(ConnectionData connectionData) throws ConnectionDataException {
        try {
            Optional<ConnectionData> oldConnectionData = connectionDataRepo.findById(connectionData.getId());

            if(oldConnectionData.isPresent()) {
                return connectionDataRepo.save(connectionData);
            } else {
                logger.info("Connection data not found to update");
                throw new ConnectionDataException("Connection data not found to update", new Throwable());
            }
        } catch(IllegalArgumentException e) {
            logger.error("Error updating connection data for id: " + connectionData.getId(), e);
            throw new ConnectionDataException("Failed to update connecion data", e);
        }
    }
}
