package com.dypcoe.qsdta.service;

import com.dypcoe.qsdta.dao.AuthUserRepository;
import com.dypcoe.qsdta.exception.dao.AuthUserException;
import com.dypcoe.qsdta.model.AuthUser;
import com.dypcoe.qsdta.model.QsdtaUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private AuthUserRepository userRepository;

    public void registerUser(AuthUser authUser) throws AuthUserException {
        try {
            if(!userRepository.existsByEmail(authUser.getEmail())) {
//                authUser.setUuid(UUID.fromString(authUser.getEmail()).toString());
                userRepository.save(authUser);
            } else {
                throw new Exception("User already exist. UUID: " + authUser.getUuid());
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new AuthUserException(e.getMessage(), e);
        }
    }

    public AuthUser loginUser(String email, String password) throws AuthUserException {
        try {
            if(userRepository.existsByEmail(email)) {
                AuthUser user = userRepository.findByEmail(email);
                if(user.getPassword().equals(password)) {
                    return user;
                } else {
                    throw new Exception("Invalid Credentials.");
                }
            } else {
                throw new Exception("User not found: " + email);
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new AuthUserException(e.getMessage(), e);

        }
    }

    public AuthUser updateUser(AuthUser user) throws AuthUserException {
        try {
            if(userRepository.existsById(user.getUuid())) {
                return userRepository.save(user);
            } else {
                throw new Exception("Invalid User Credential. UUID: " + user.getUuid());
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new AuthUserException(e.getMessage(), e);
        }
    }

    public QsdtaUser getUser(String uuid) throws AuthUserException {
        Optional<AuthUser> user = userRepository.findById(uuid);

        try {
            if(user.isEmpty()) {
                throw new Exception("User not found. UUID: " + uuid);
            } else {
                QsdtaUser qsdtaUser = new QsdtaUser();
                setQsdtaUserFromAuthUser(qsdtaUser, user.get());
                return qsdtaUser;
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new AuthUserException(e.getMessage(), e);
        }
    }

    public AuthUser getAuthUser(String uuid) throws AuthUserException {
        Optional<AuthUser> user = userRepository.findById(uuid);

        try {
            if(user.isEmpty()) {
                throw new Exception("User not found. UUID: " + uuid);
            } else {
                return user.get();
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new AuthUserException(e.getMessage(), e);
        }
    }

    public List<QsdtaUser> getUserPage(int page, int size) throws AuthUserException {
        Page<AuthUser>  authUsersPage = userRepository.findAllByOrderByFirstNameAsc(PageRequest.of(page, size));
        List<QsdtaUser> qsdtaUserList = new ArrayList<>(size);

        try {
            if(authUsersPage.isEmpty())
            throw new Exception("No users found");

            for(AuthUser authUser : authUsersPage.getContent()) {
                QsdtaUser qsdtaUser = new QsdtaUser();
                setQsdtaUserFromAuthUser(qsdtaUser, authUser);
                qsdtaUserList.add(qsdtaUser);
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new AuthUserException(e.getMessage(), e);
        }
        return qsdtaUserList;
    }

    public void deleteAuthUser(String uuid) throws AuthUserException {
        try {
            if(userRepository.existsById(uuid)) {
                 userRepository.deleteById(uuid);
             }
             else
                 throw new Exception("User not found. UUID: " + uuid);
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new AuthUserException(e.getMessage(), e);
        }
    }

    private void setQsdtaUserFromAuthUser(QsdtaUser qsdtaUser, AuthUser authUser) {
        qsdtaUser.setUuid(authUser.getUuid());
        qsdtaUser.setEmail(authUser.getEmail());
        qsdtaUser.setFirstName(authUser.getFirstName());
        qsdtaUser.setLastName(authUser.getLastName());
        qsdtaUser.setImageURL(authUser.getPicLink());
    }
}
