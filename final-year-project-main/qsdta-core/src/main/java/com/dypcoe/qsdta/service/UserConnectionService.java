package com.dypcoe.qsdta.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dypcoe.qsdta.dao.UserConnectionRepo;
import com.dypcoe.qsdta.exception.dao.UserConnectionException;
import com.dypcoe.qsdta.model.AuthUser;
import com.dypcoe.qsdta.model.UserConnection;

@Service
public class UserConnectionService {
    private static Logger logger = LoggerFactory.getLogger(UserConnectionService.class);
    @Autowired
    private UserConnectionRepo userConnectionRepo;

    public UserConnection insertUserConnection(UserConnection userConnection) throws UserConnectionException {
        try {
            if(!userConnectionRepo.existsByAuthUser1AndAuthUser2(userConnection.getAuthUser1(), userConnection.getAuthUser2())) {
                return userConnectionRepo.save(userConnection);
            } else {
                logger.info("User Connection already exist");
                throw new RuntimeException("User connection already exist");
            }
        } catch(Exception e) {
            logger.error("Error inserting user connection", e);
            throw new UserConnectionException("Failed to insert user connection Or connection already exist", e);
        }
    }

    public List<UserConnection> getUserConnections(AuthUser authUser) throws UserConnectionException {
        try {
            if(userConnectionRepo.existsByAuthUser1OrAuthUser2(authUser, authUser)) {
                return userConnectionRepo.findAllByAuthUser1OrAuthUser2(authUser, authUser);
            } else {
                logger.info("User connection not found for auth user: " + authUser.getEmail());
                throw new UserConnectionException("User connection not found", new Throwable());
            }
        } catch(IllegalArgumentException e) {
            logger.error("Error retrieving user connection", e);
            throw new UserConnectionException("Failed to get user connection", e);
        }
    }

    public void deleteUserConnection(UserConnection userConnection) throws UserConnectionException {
        try {
            if (userConnectionRepo.existsById(userConnection.getId())) {
                userConnectionRepo.delete(userConnection);
            } else {
                logger.info("User connection not found to delete");
                throw new UserConnectionException("User connection not found", new Throwable());
            }
        } catch(IllegalArgumentException e) {
            logger.error("Error deleting user connection", e);
            throw new UserConnectionException("Failed to delete user connection", e);
        }
    }
}
