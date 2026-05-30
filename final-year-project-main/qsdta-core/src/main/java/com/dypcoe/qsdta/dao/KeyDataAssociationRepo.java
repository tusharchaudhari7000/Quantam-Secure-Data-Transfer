package com.dypcoe.qsdta.dao;

import com.dypcoe.qsdta.model.ConnectionData;
import com.dypcoe.qsdta.model.ConnectionKey;
import com.dypcoe.qsdta.model.KeyDataAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyDataAssociationRepo extends JpaRepository<KeyDataAssociation, Integer> {
    public void deleteByConnectionKey(ConnectionKey connectionKey);

    public void deleteAllByConnectionKey(ConnectionKey connectionKey);

    public void deleteByConnectionData(ConnectionData connectionData);
}
