CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    capacity INTEGER NOT NULL
);

CREATE TYPE booking_status AS ENUM ('active', 'cancelled', 'archived');

CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    room_id INTEGER NOT NULL REFERENCES rooms(id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL
);
