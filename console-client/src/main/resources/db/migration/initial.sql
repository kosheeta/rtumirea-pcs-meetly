-- Пересоздаёт таблицы и удаляет все существующие данные приложения.
DROP TABLE IF EXISTS booking_participants, bookings, rooms, users;
DROP TYPE IF EXISTS booking_status;

-- crypt()/gen_salt() для хранения паролей сервисных аккаунтов в виде bcrypt-хешей
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'user' CHECK (role IN ('user', 'service_account')),
    -- Задан только у сервисных аккаунтов: под ними входят в админку javafx-client
    password_hash VARCHAR(255)
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
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    room_id INTEGER NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    title VARCHAR(255),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL
);

-- Приглашённые на встречу, кроме организатора (он указан в bookings.user_id)
CREATE TABLE booking_participants (
    booking_id INTEGER NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (booking_id, user_id)
);
