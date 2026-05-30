package com.dypcoe.qsdta.dao;

import com.dypcoe.qsdta.model.ConnectionKey;
import com.dypcoe.qsdta.model.UserConnection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionKeyRepo extends JpaRepository<ConnectionKey, Integer> {
    public ConnectionKey findByUserConnection(UserConnection userConnection);
    
    public boolean existsByUserConnection(UserConnection userConnection);
}
