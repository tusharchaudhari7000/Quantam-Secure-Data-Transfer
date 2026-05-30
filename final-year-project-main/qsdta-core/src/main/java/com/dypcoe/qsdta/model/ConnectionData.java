package com.dypcoe.qsdta.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "connection_data")
public class ConnectionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;
    @ManyToOne
    @JoinColumn(name = "connection_id", referencedColumnName = "id", nullable = false)
    private UserConnection userConnection;
    @Column(name = "data_type", nullable = false)
    private String dataType;
    @Column(name = "byte_data", nullable = false)
    private byte[] byteData;
    @Column(name = "owner", nullable = false)
    private String owner;

    public void setByteData(byte[] byteData) {
        this.byteData = (byteData != null) ? byteData.clone() : new byte[0];
    }    

    public byte[] getByteData() {
        return (byteData != null) ? byteData.clone() : new byte[0];
    }

    public UserConnection getUserConnection() {
        return (userConnection == null) ? null : new UserConnection(userConnection);
    }

    public void setUserConnection(UserConnection userConnection) {
        this.userConnection = (userConnection != null) ? new UserConnection(userConnection) : null;
    }
}
