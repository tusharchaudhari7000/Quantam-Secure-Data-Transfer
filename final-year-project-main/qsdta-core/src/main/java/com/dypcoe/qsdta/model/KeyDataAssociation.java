package com.dypcoe.qsdta.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "key_data_associated")
public class KeyDataAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "key_id", referencedColumnName = "id", nullable = false)
    private ConnectionKey connectionKey;
    @ManyToOne
    @JoinColumn(name = "connection_data_id", referencedColumnName = "id", nullable = false)
    private ConnectionData connectionData;

    public void setConnectionData(ConnectionData connectionData) {
        this.connectionData = (connectionData != null) ? connectionData : null;
    }

    public void setConnectionKey(ConnectionKey connectionKey) {
        this.connectionKey = (connectionKey != null) ? connectionKey : null;
    }

    public ConnectionData getConnectionData() {
        return (this.connectionData == null) ? null : this.connectionData;
    }

    public ConnectionKey getConnectionKey() {
        return (this.connectionKey == null) ? null : this.connectionKey;
    }
}
