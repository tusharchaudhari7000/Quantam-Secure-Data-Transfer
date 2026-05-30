package com.dypcoe.qsdta.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "connection_keys")
public class ConnectionKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "connection_id", referencedColumnName = "id", nullable = false)
    private UserConnection userConnection;
    @Column(name = "created_timestamp", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTimestamp;
    @Column(name = "expiry_timestamp")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryTimestamp;
    @Column(name = "crypto_key", nullable = false)
    private byte[] cryptoKey;

    public void setCryptoKey(byte[] cryptoKey) {
        this.cryptoKey = (cryptoKey != null) ? cryptoKey.clone() : null;
    }

    public byte[] getCryptoKey() {
        return (cryptoKey == null) ? null : cryptoKey.clone();
    }

    public void setUserConnection(UserConnection userConnection) {
        this.userConnection = (userConnection != null) ? new UserConnection(userConnection) : null;
    }

    public UserConnection getUserConnection() {
        return (userConnection == null) ? null : new UserConnection(userConnection);
    }
}
