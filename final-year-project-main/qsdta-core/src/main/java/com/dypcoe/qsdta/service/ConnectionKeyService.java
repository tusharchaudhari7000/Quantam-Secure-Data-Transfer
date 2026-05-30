package com.dypcoe.qsdta.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dypcoe.qsdta.dao.ConnectionKeyRepo;
import com.dypcoe.qsdta.exception.dao.ConnectionKeyException;
import com.dypcoe.qsdta.model.ConnectionKey;
import com.dypcoe.qsdta.model.UserConnection;

@Service
public class ConnectionKeyService {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionKeyService.class);
    @Autowired
    private ConnectionKeyRepo connectionKeyRepo;
    
    public ConnectionKey insertConnectionKey(ConnectionKey connectionKey) throws ConnectionKeyException {
        try {
            return connectionKeyRepo.save(connectionKey);
        } catch(IllegalArgumentException e) {
            logger.error("Error inserting connection key", e);
            throw new ConnectionKeyException("Failed to insert connection key retry!", e);
        }
    }

    public ConnectionKey getConnectionKey(UserConnection userConnection) throws ConnectionKeyException {
        try {
            if(connectionKeyRepo.existsByUserConnection(userConnection)) {
                return connectionKeyRepo.findByUserConnection(userConnection);
            } else {
                throw new ConnectionKeyException("No connection key found for: " + userConnection.getId(), new Throwable());
            }
        } catch(IllegalArgumentException e) {
            logger.error("Error retrieving connection key", e);
            throw new ConnectionKeyException("Failed to get connection key for: " + userConnection.getId(), e);
        }
    }

    public void deleteConnectionKey(int keyId) throws ConnectionKeyException {
        try {
            connectionKeyRepo.deleteById(keyId);
        } catch(IllegalArgumentException e) {
            logger.error("Error deleting connection key", e);
            throw new ConnectionKeyException("Failed to delete connection key!", e);
        }
    }

    public ConnectionKey updateConnectionKeyExpiry(int keyId) throws ConnectionKeyException {
        try {
            Optional<ConnectionKey> optionalKey = connectionKeyRepo.findById(keyId);
            if(optionalKey.isPresent()) {
                ConnectionKey connectionKey = optionalKey.get();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiry = connectionKey.getExpiryTimestamp();

                if(expiry == null) {
                    connectionKey.setExpiryTimestamp(now.plusDays(30));
                    return connectionKeyRepo.save(connectionKey);
                }

                long dayDifference = Duration.between(now, expiry).toDays();

                if(dayDifference != 30) {
                    connectionKey.setExpiryTimestamp(expiry.plusDays(30));
                    return connectionKeyRepo.save(connectionKey);
                } else {
                    return connectionKey;
                }

            } else {
                throw new ConnectionKeyException("Connection key not found.", new Throwable());
            }
        } catch(Exception e) {
            logger.error("Error updating connection key expiry", e);
            throw new ConnectionKeyException("Failed to update connection key expiry", e);
        }
    }
}
