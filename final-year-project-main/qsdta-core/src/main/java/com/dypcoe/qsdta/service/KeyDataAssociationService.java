package com.dypcoe.qsdta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dypcoe.qsdta.dao.KeyDataAssociationRepo;
import com.dypcoe.qsdta.exception.dao.KeyDataAssociationException;
import com.dypcoe.qsdta.model.ConnectionData;
import com.dypcoe.qsdta.model.ConnectionKey;
import com.dypcoe.qsdta.model.KeyDataAssociation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KeyDataAssociationService {
    private static final Logger logger = LoggerFactory.getLogger(KeyDataAssociationService.class);
    @Autowired
    private KeyDataAssociationRepo keyDataAssociationRepo;

    public KeyDataAssociation inserKeyDataAssociation(KeyDataAssociation keyDataAssociation) throws KeyDataAssociationException {
        try {
            return keyDataAssociationRepo.save(keyDataAssociation);
        } catch(IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new KeyDataAssociationException("KeyDataAssociated Insertion Failed.", e);
        }
    }

    public void deleteByConnectionData(ConnectionData connectionData) throws KeyDataAssociationException {
        try {
            keyDataAssociationRepo.deleteByConnectionData(connectionData);
        } catch(IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new KeyDataAssociationException("Specified conncection_data_id not found. Key ID: " + connectionData.getId(), e);
        }
    }

    @Transactional
    public void deleteKeyDataAssociationByConnectionKey(ConnectionKey connectionKey) throws KeyDataAssociationException {
        try {
            keyDataAssociationRepo.deleteByConnectionKey(connectionKey);
        } catch(IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new KeyDataAssociationException("Specified key_id not found. Key ID: " + connectionKey.getId(), e);
        }
    }

    @Transactional
    public void deleteAllKeyDataAssociation(ConnectionKey connectionKey) throws KeyDataAssociationException {
        try {
            keyDataAssociationRepo.deleteAllByConnectionKey(connectionKey);
        } catch(IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new KeyDataAssociationException("Specified key_id not found. Key ID: " + connectionKey.getId(), e);
        }
    }
}
