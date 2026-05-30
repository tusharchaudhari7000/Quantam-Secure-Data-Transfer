package com.dypcoe.qsdta.dao;

import com.dypcoe.qsdta.model.ConnectionData;
import com.dypcoe.qsdta.model.UserConnection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionDataRepo extends JpaRepository<ConnectionData, Integer> {
    public List<ConnectionData> findAllByUserConnection(UserConnection userConnection);

    public List<ConnectionData> findAllByUserConnectionAndDataType(UserConnection userConnection, String dataType);

    public boolean existsByUserConnection(UserConnection userConnection);
    
    public void deleteAllByUserConnection(UserConnection userConnection);
}
