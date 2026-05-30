package com.dypcoe.qsdta.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dypcoe.qsdta.model.AuthUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, String> {
    public AuthUser findByEmail(String email);

    public boolean existsByEmail(String email);
    
    public Page<AuthUser> findAllByOrderByFirstNameAsc(Pageable pageable);
}
