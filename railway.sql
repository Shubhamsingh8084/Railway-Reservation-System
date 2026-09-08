-- =========================================
-- Railway Reservation System Database
-- =========================================

CREATE DATABASE IF NOT EXISTS railway_db;

USE railway_db;


-- =========================================
-- 1. USERS TABLE
-- =========================================

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(15)
);


-- =========================================
-- 2. ADMINS TABLE
-- =========================================

CREATE TABLE admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    role VARCHAR(50) DEFAULT 'ADMIN',

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);


-- =========================================
-- 3. TRAINS TABLE
-- =========================================

CREATE TABLE trains (
    train_id INT PRIMARY KEY AUTO_INCREMENT,
    train_number VARCHAR(20) UNIQUE NOT NULL,
    train_name VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL
);


-- =========================================
-- 4. PASSENGERS TABLE
-- =========================================

CREATE TABLE passengers (
    passenger_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10),
    seat_preference VARCHAR(20)
);


-- =========================================
-- 5. BOOKINGS TABLE
-- =========================================

CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    train_id INT NOT NULL,
    pnr VARCHAR(20) UNIQUE NOT NULL,
    journey_date DATE NOT NULL,
    total_fare DOUBLE NOT NULL,
    status VARCHAR(30) DEFAULT 'CONFIRMED',

    FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    FOREIGN KEY (train_id)
        REFERENCES trains(train_id)
);


-- =========================================
-- 6. TICKETS TABLE
-- =========================================

CREATE TABLE tickets (
    ticket_id INT PRIMARY KEY AUTO_INCREMENT,
    pnr VARCHAR(20) NOT NULL,
    train_number VARCHAR(20) NOT NULL,
    train_name VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    journey_date DATE NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    seat_number INT NOT NULL,
    fare DOUBLE NOT NULL,
    status VARCHAR(30) DEFAULT 'CONFIRMED'
);


-- =========================================
-- SAMPLE USERS
-- =========================================

INSERT INTO users
(name, email, password, phone)
VALUES
('Shubham Singh', 'shubham@gmail.com', '1234', '9876543210'),
('Rahul Kumar', 'rahul@gmail.com', '1234', '9876543211');


-- =========================================
-- SAMPLE ADMIN
-- =========================================

INSERT INTO admins
(user_id, role)
VALUES
(1, 'ADMIN');


-- =========================================
-- SAMPLE TRAINS
-- =========================================

INSERT INTO trains
(train_number, train_name, source, destination,
 total_seats, available_seats)
VALUES

('12301',
 'Rajdhani Express',
 'Delhi',
 'Mumbai',
 100,
 100),

('11123',
 'Gwalior Barauni Mail',
 'Gwalior',
 'Siwan',
 120,
 120),

('12951',
 'Mumbai Rajdhani',
 'Mumbai',
 'Delhi',
 100,
 100),

('12801',
 'Puri Express',
 'Puri',
 'New Delhi',
 120,
 120);


-- =========================================
-- CHECK DATA
-- =========================================

SELECT * FROM users;

SELECT * FROM admins;

SELECT * FROM trains;

SELECT * FROM bookings;

SELECT * FROM passengers;

SELECT * FROM tickets;