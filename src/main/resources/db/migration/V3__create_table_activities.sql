CREATE TABLE activities (
    id BINARY(16)  DEFAULT (UUID()) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    occurs_at TIMESTAMP NOT NULL,
    trip_id BINARY(16),
    FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE
);