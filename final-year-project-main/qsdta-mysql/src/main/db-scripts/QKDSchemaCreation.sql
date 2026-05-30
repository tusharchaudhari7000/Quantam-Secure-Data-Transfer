CREATE DATABASE qsdta;
USE qsdta;

CREATE TABLE authorized_users IF NOT EXIST(
    uuid VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    f_name VARCHAR(255),
    l_name VARCHAR(255),
    image_url VARCHAR(255)
);

CREATE TABLE user_connections IF NOT EXIST(
    id INT AUTO INCREMENT PRIMARY KEY,
    user1_uuid VARCHAR(255) FOREIGN KEY REFERENCES ('authorized_users'),
    user2_uuid VARCHAR(255) FOREIGN KEY REFERENCES ('authorized_users')
);

CREATE TABLE connection_data IF NOT EXIST(
    id INT AUTO INCREMENT PRIMARY KEY,
    connection_id VARCHAR(255) FOREIGN KEY REFERENCES ('user_connections'),
    data_type VARCHAR(255),
    byte_data BLOB NOT NULL
);

CREATE TABLE connection_keys IF NOT EXIST(
    id INT AUTO INCREMENT PRIMARY KEY,
    connection_id VARCHAR(255) FOREIGN KEY REFERENCES ('user_connections'),
    created_timestamp DATE NOT NULL,
    expiry_timestamp DATE,
    crypto_key VARCHAR(255) NOT NULL
);

CREATE TABLE key_data_associated IF NOT EXIST(
    key_id VARCHAR(255) FOREIGN KEY REFERENCES('connection_keys'),
    connection_data_id INT FOREIGN KEY REFERENCES('connection_data')
);
