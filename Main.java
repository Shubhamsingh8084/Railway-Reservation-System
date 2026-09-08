package com.railway;

import com.railway.exception.BookingNotFoundException;
import com.railway.exception.InvalidUserException;
import com.railway.exception.SeatNotAvailableException;
import com.railway.exception.TrainNotFoundException;
import com.railway.model.Booking;
import com.railway.model.Passenger;
import com.railway.model.Train;
import com.railway.model.User;
import com.railway.service.BookingService;
import com.railway.service.TicketService;
import com.railway.service.TrainService;
import com.railway.service.UserService;
import com.railway.thread.NotificationTask;
import com.railway.util.InputUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final UserService userService = new UserService();

    private static final TrainService trainService = new TrainService();

    private static final BookingService bookingService = new BookingService();

    private static final TicketService ticketService = new TicketService();

    private static User currentUser = null;

    public static void main(String[] args) {

        System.out.println();
        System.out.println("========================================");
        System.out.println("   WELCOME TO RAILWAY RESERVATION SYSTEM");
        System.out.println("========================================");

        boolean running = true;

        while (running) {

            showMainMenu();

            int choice = InputUtil.getInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    registerUser();
                    break;

                case 2:
                    loginUser();
                    break;

                case 3:
                    searchTrain();
                    break;

                case 4:

                    if (checkLogin()) {
                        bookTicket();
                    }

                    break;

                case 5:

                    if (checkLogin()) {
                        viewMyBookings();
                    }

                    break;

                case 6:
                    checkPNR();
                    break;

                case 7:

                    if (checkLogin()) {
                        cancelBooking();
                    }

                    break;

                case 8:
                    showAllTrains();
                    break;

                case 9:
                    System.out.println();
                    System.out.println(
                            "Thank you for using Railway Reservation System!");

                    running = false;
                    break;

                default:

                    System.out.println(
                            "Invalid choice! Please try again.");
            }
        }

        InputUtil.closeScanner();
    }

    // =========================================
    // MAIN MENU
    // =========================================

    private static void showMainMenu() {

        System.out.println();
        System.out.println("------------ MAIN MENU ------------");

        System.out.println("1. Register User");
        System.out.println("2. Login");
        System.out.println("3. Search Train");
        System.out.println("4. Book Ticket");
        System.out.println("5. My Bookings");
        System.out.println("6. Check PNR");
        System.out.println("7. Cancel Booking");
        System.out.println("8. View All Trains");
        System.out.println("9. Exit");

        System.out.println("-----------------------------------");
    }

    // =========================================
    // REGISTER USER
    // =========================================

    private static void registerUser() {

        try {

            System.out.println();
            System.out.println("------ USER REGISTRATION ------");

            String name = InputUtil.getString("Enter Name: ");

            String email = InputUtil.getString("Enter Email: ");

            String password = InputUtil.getString("Enter Password: ");

            String phone = InputUtil.getString("Enter Phone Number: ");

            User user = new User(name, email, password, phone);

            boolean registered = userService.registerUser(user);

            if (registered) {

                System.out.println(
                        "User registered successfully!");

            } else {

                System.out.println(
                        "User registration failed!");
            }

        } catch (InvalidUserException e) {

            System.out.println(
                    "Error: " + e.getMessage());
        }
    }

    // =========================================
    // LOGIN
    // =========================================

    private static void loginUser() {

        try {

            System.out.println();
            System.out.println("------------ LOGIN ------------");

            String email = InputUtil.getString("Enter Email: ");

            String password = InputUtil.getString("Enter Password: ");

            currentUser = userService.login(email, password);

            System.out.println();
            System.out.println(
                    "Login successful! Welcome "
                            + currentUser.getName());

        } catch (InvalidUserException e) {

            System.out.println(
                    "Login failed: "
                            + e.getMessage());
        }
    }

    // =========================================
    // SEARCH TRAIN
    // =========================================

    private static void searchTrain() {

        try {

            System.out.println();
            System.out.println("--------- SEARCH TRAIN ---------");

            String source = InputUtil.getString("Enter Source: ");

            String destination = InputUtil.getString("Enter Destination: ");

            List<Train> trains = trainService.searchTrains(
                    source,
                    destination);

            System.out.println();
            System.out.println("Available Trains:");

            trains.forEach(train -> {

                System.out.println(
                        "--------------------------------");

                System.out.println(
                        "Train ID: "
                                + train.getTrainId());

                System.out.println(
                        "Train Number: "
                                + train.getTrainNumber());

                System.out.println(
                        "Train Name: "
                                + train.getTrainName());

                System.out.println(
                        "Route: "
                                + train.getSource()
                                + " → "
                                + train.getDestination());

                System.out.println(
                        "Available Seats: "
                                + train.getAvailableSeats());
            });

        } catch (TrainNotFoundException e) {

            System.out.println(
                    "Error: " + e.getMessage());
        }
    }

    // =========================================
    // BOOK TICKET
    // =========================================

    private static void bookTicket() {

        try {

            System.out.println();
            System.out.println("--------- BOOK TICKET ---------");

            int trainId = InputUtil.getInt(
                    "Enter Train ID: ");

            String dateInput = InputUtil.getString(
                    "Enter Journey Date (YYYY-MM-DD): ");

            LocalDate journeyDate = LocalDate.parse(dateInput);

            int passengerCount = InputUtil.getInt(
                    "Enter Number of Passengers (1-6): ");

            List<Passenger> passengers = new ArrayList<>();

            for (int i = 1; i <= passengerCount; i++) {

                System.out.println();
                System.out.println(
                        "Passenger " + i + " Details");

                String name = InputUtil.getString(
                        "Name: ");

                int age = InputUtil.getInt(
                        "Age: ");

                String gender = InputUtil.getString(
                        "Gender: ");

                String preference = InputUtil.getString(
                        "Seat Preference: ");

                Passenger passenger = new Passenger();

                passenger.setName(name);
                passenger.setAge(age);
                passenger.setGender(gender);
                passenger.setSeatPreference(
                        preference);

                passengers.add(passenger);
            }

            // Create Booking
            Booking booking = bookingService.createBooking(
                    currentUser.getUserId(),
                    trainId,
                    journeyDate,
                    passengers);

            System.out.println();
            System.out.println(
                    "================================");

            System.out.println(
                    "BOOKING SUCCESSFUL!");

            System.out.println(
                    "PNR: " + booking.getPnr());

            System.out.println(
                    "Total Fare: ₹"
                            + booking.getTotalFare());

            System.out.println(
                    "Status: "
                            + booking.getStatus());

            System.out.println(
                    "================================");

            // Notification Thread
            Thread notificationThread = new Thread(
                    new NotificationTask(
                            "Your ticket has been booked successfully! "
                                    + "PNR: "
                                    + booking.getPnr()));

            notificationThread.start();

        } catch (InvalidUserException | TrainNotFoundException | SeatNotAvailableException e) {

            System.out.println(
                    "Booking failed: "
                            + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "Invalid input: "
                            + e.getMessage());
        }
    }

    // =========================================
    // VIEW MY BOOKINGS
    // =========================================

    private static void viewMyBookings() {

        try {

            List<Booking> bookings = bookingService.getUserBookings(
                    currentUser.getUserId());

            System.out.println();
            System.out.println(
                    "--------- MY BOOKINGS ---------");

            if (bookings.isEmpty()) {

                System.out.println(
                        "No bookings found!");

                return;
            }

            bookings.forEach(booking -> {

                System.out.println(
                        "--------------------------------");

                System.out.println(
                        "Booking ID: "
                                + booking.getBookingId());

                System.out.println(
                        "PNR: "
                                + booking.getPnr());

                System.out.println(
                        "Journey Date: "
                                + booking.getJourneyDate());

                System.out.println(
                        "Passengers: "
                                + booking.getPassengers().size());

                System.out.println(
                        "Total Fare: ₹"
                                + booking.getTotalFare());

                System.out.println(
                        "Status: "
                                + booking.getStatus());
            });

        } catch (InvalidUserException e) {

            System.out.println(
                    "Error: " + e.getMessage());
        }
    }

    // =========================================
    // CHECK PNR
    // =========================================

    private static void checkPNR() {

        try {

            System.out.println();
            System.out.println(
                    "--------- CHECK PNR ---------");

            String pnr = InputUtil.getString(
                    "Enter PNR Number: ");

            List<com.railway.model.Ticket> tickets = ticketService.generateTickets(pnr);

            ticketService.printTickets(tickets);

        } catch (BookingNotFoundException | TrainNotFoundException e) {

            System.out.println(
                    "Error: " + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage());
        }
    }

    // =========================================
    // CANCEL BOOKING
    // =========================================

    private static void cancelBooking() {

        try {

            System.out.println();
            System.out.println(
                    "--------- CANCEL BOOKING ---------");

            int bookingId = InputUtil.getInt(
                    "Enter Booking ID: ");

            boolean cancelled = bookingService.cancelBooking(
                    bookingId);

            if (cancelled) {

                System.out.println(
                        "Booking cancelled successfully!");

            } else {

                System.out.println(
                        "Booking is already cancelled!");
            }

        } catch (BookingNotFoundException | TrainNotFoundException e) {

            System.out.println(
                    "Cancellation failed: "
                            + e.getMessage());
        }
    }

    // =========================================
    // VIEW ALL TRAINS
    // =========================================

    private static void showAllTrains() {

        List<Train> trains = trainService.getAllTrains();

        System.out.println();
        System.out.println(
                "--------- ALL TRAINS ---------");

        if (trains.isEmpty()) {

            System.out.println(
                    "No trains available!");

            return;
        }

        trains.forEach(train -> {

            System.out.println(
                    "--------------------------------");

            System.out.println(
                    "ID: " + train.getTrainId());

            System.out.println(
                    "Number: "
                            + train.getTrainNumber());

            System.out.println(
                    "Name: "
                            + train.getTrainName());

            System.out.println(
                    "Route: "
                            + train.getSource()
                            + " → "
                            + train.getDestination());

            System.out.println(
                    "Total Seats: "
                            + train.getTotalSeats());

            System.out.println(
                    "Available Seats: "
                            + train.getAvailableSeats());
        });
    }

    // =========================================
    // CHECK LOGIN
    // =========================================

    private static boolean checkLogin() {

        if (currentUser == null) {

            System.out.println();
            System.out.println(
                    "Please login first!"
            );

            return false;
        }

        return true;
    }
}