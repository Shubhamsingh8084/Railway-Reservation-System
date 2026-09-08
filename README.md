# 🚆 Railway Reservation System

A **console-based Railway Reservation System** developed using **Core Java** and **JDBC**. This project allows users to register, log in, search trains, book tickets, check PNR status, view bookings, and cancel reservations.

The project demonstrates important Java concepts such as **OOPs, Collection Framework, Generics, JDBC, Java 8 Features, Multithreading, and Exception Handling**.

---

## 📌 Features

### 👤 User Features

- User Registration
- User Login
- Search Trains
- View All Available Trains
- Book Railway Tickets
- Add Multiple Passengers
- Automatic PNR Generation
- Fare Calculation
- Check PNR Status
- View My Bookings
- Cancel Booking
- Automatic Seat Release After Cancellation

---

### 🚆 Train Features

- Add Train
- Search Train by Source and Destination
- Find Train by ID
- Find Train by Train Number
- View All Trains
- Update Train Details
- Delete Train
- Check Available Seats
- Reserve Seats
- Release Seats After Cancellation

---

### 🎫 Ticket Features

- Generate Ticket using PNR
- Display Passenger Details
- Display Train Details
- Display Seat Number
- Display Journey Date
- Display Fare
- Display Booking Status

---

### 🔐 Admin Features

- Admin Data Access
- Admin Role Management
- Check Admin Status
- Update Admin Role

---

## 🛠️ Technologies Used

| Technology | Purpose |
|-----------|---------|
| Java | Main Programming Language |
| Core Java | Application Logic |
| OOPs | Object-Oriented Design |
| JDBC | Database Connectivity |
| MySQL | Database |
| Collection Framework | Managing Lists of Data |
| Generics | Type-Safe Collections |
| Java 8 | Stream API, Lambda Expressions, Optional |
| Multithreading | Booking and Notification Tasks |
| Exception Handling | Custom Exception Management |

---

# 🏗️ Project Architecture

```text
                ┌───────────────┐
                │   Main.java   │
                └───────┬───────┘
                        │
                        ▼
                ┌───────────────┐
                │ Service Layer │
                └───────┬───────┘
                        │
                        ▼
                ┌───────────────┐
                │   DAO Layer   │
                └───────┬───────┘
                        │
                        ▼
                ┌───────────────┐
                │     JDBC      │
                └───────┬───────┘
                        │
                        ▼
                ┌───────────────┐
                │    MySQL DB   │
                └───────────────┘
