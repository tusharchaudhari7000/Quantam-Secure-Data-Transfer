package com.dypcoe.qsdta.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Table(name = "user_connections")
public class UserConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id; // connection id generated after connection
    @ManyToOne
    @JoinColumn(name = "user1_uuid", referencedColumnName = "uuid", nullable = false)
    private AuthUser authUser1;
    @ManyToOne
    @JoinColumn(name = "user2_uuid", referencedColumnName = "uuid", nullable = false)
    private AuthUser authUser2;


    public void setAuthUser1(AuthUser authUser1) {
        this.authUser1 = (authUser1 != null) ? authUser1 : null;
    }

    public void setAuthUser2(AuthUser authUser2) {
        this.authUser2 = (authUser2 != null) ? authUser2 : null;
    }

    public AuthUser getAuthUser1() {
        return (authUser1 == null) ? null : authUser1;
    }

    public AuthUser getAuthUser2() {
        return (authUser2 == null) ? null : authUser2;
    }

    public Integer getId(){
        return id;
    }

    public UserConnection(UserConnection other) {
        this.id = other.id;
        this.authUser1 = other.authUser1; // or clone if needed
        this.authUser2 = other.authUser2; // or clone if needed
    }

    public UserConnection() {}
}
