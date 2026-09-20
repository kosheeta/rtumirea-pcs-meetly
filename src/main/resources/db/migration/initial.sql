-- Пересоздаёт таблицы и удаляет все существующие данные приложения.
DROP TABLE IF EXISTS bookings, rooms, users;
DROP TYPE IF EXISTS booking_status;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    capacity INTEGER NOT NULL,
    address VARCHAR(255)
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
