CREATE TABLE participants (
    id BINARY(16)  DEFAULT (UUID()) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_confirmed BOOLEAN NOT NULL,
    trip_id BINARY(16) ,
    FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE
);