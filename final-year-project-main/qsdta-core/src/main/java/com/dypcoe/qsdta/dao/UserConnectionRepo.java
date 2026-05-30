package com.dypcoe.qsdta.dao;

import com.dypcoe.qsdta.model.AuthUser;
import com.dypcoe.qsdta.model.UserConnection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConnectionRepo extends JpaRepository<UserConnection, Integer> {
    public boolean existsByAuthUser1AndAuthUser2(AuthUser authUser1, AuthUser authUser2);

    public boolean existsByAuthUser1OrAuthUser2(AuthUser authUser1, AuthUser authUser2);

    public List<UserConnection> findAllByAuthUser1OrAuthUser2(AuthUser authUser1, AuthUser authUser2);
}
