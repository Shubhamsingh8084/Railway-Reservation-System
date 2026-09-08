package com.railway.service;

import com.railway.dao.BookingDAO;
import com.railway.exception.BookingNotFoundException;
import com.railway.exception.SeatNotAvailableException;
import com.railway.exception.TrainNotFoundException;
import com.railway.exception.InvalidUserException;
import com.railway.model.Booking;
import com.railway.model.Passenger;
import com.railway.model.Train;
import com.railway.model.User;
import com.railway.util.PNRGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BookingService {

    private final BookingDAO bookingDAO;
    private final UserService userService;
    private final TrainService trainService;

    // Fare per passenger
    private static final double FARE_PER_PASSENGER = 500.0;

    // Constructor
    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.userService = new UserService();
        this.trainService = new TrainService();
    }

    // =========================================
    // Create Booking
    // =========================================

    public Booking createBooking(
            int userId,
            int trainId,
            LocalDate journeyDate,
            List<Passenger> passengers)
            throws InvalidUserException,
            TrainNotFoundException,
            SeatNotAvailableException {

        // -------------------------
        // Validate User
        // -------------------------

        User user = userService.getUserById(userId);

        if (user == null) {
            throw new InvalidUserException(
                    "Invalid user!");
        }

        // -------------------------
        // Validate Journey Date
        // -------------------------

        if (journeyDate == null) {
            throw new IllegalArgumentException(
                    "Journey date cannot be null!");
        }

        if (journeyDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Journey date cannot be in the past!");
        }

        // -------------------------
        // Validate Passengers
        // -------------------------

        validatePassengers(passengers);

        int passengerCount = passengers.size();

        // -------------------------
        // Check Train
        // -------------------------

        Train train = trainService.getTrainById(trainId);

        // -------------------------
        // Check Seats
        // -------------------------

        trainService.checkSeats(
                trainId,
                passengerCount);

        // -------------------------
        // Calculate Fare
        // -------------------------

        double totalFare = calculateFare(passengerCount);

        // -------------------------
        // Generate PNR
        // -------------------------

        String pnr = PNRGenerator.generatePNR();

        // -------------------------
        // Create Booking Object
        // -------------------------

        Booking booking = new Booking();

        booking.setUserId(userId);
        booking.setTrainId(trainId);
        booking.setPnr(pnr);
        booking.setJourneyDate(journeyDate);
        booking.setPassengers(passengers);
        booking.setTotalFare(totalFare);
        booking.setStatus("CONFIRMED");

        // -------------------------
        // Reserve Seats
        // -------------------------

        boolean seatsReserved = trainService.reserveSeats(
                trainId,
                passengerCount);

        if (!seatsReserved) {

            throw new SeatNotAvailableException(
                    "Unable to reserve seats!");
        }

        // -------------------------
        // Save Booking
        // -------------------------

        boolean saved = bookingDAO.createBooking(booking);

        if (!saved) {

            // Release seats if booking fails
            trainService.releaseSeats(
                    trainId,
                    passengerCount);

            throw new RuntimeException(
                    "Booking could not be completed!");
        }

        return booking;
    }

    // =========================================
    // Get Booking By ID
    // =========================================

    public Booking getBookingById(int bookingId)
            throws BookingNotFoundException {

        Optional<Booking> booking = bookingDAO.findById(bookingId);

        if (booking.isEmpty()) {

            throw new BookingNotFoundException(
                    "Booking not found with ID: "
                            + bookingId);
        }

        return booking.get();
    }

    // =========================================
    // Get Booking By PNR
    // =========================================

    public Booking getBookingByPNR(String pnr)
            throws BookingNotFoundException {

        if (pnr == null || pnr.trim().isEmpty()) {

            throw new BookingNotFoundException(
                    "PNR cannot be empty!");
        }

        Optional<Booking> booking = bookingDAO.findByPNR(pnr);

        if (booking.isEmpty()) {

            throw new BookingNotFoundException(
                    "Booking not found with PNR: "
                            + pnr);
        }

        return booking.get();
    }

    // =========================================
    // Get User Bookings
    // =========================================

    public List<Booking> getUserBookings(int userId)
            throws InvalidUserException {

        userService.getUserById(userId);

        return bookingDAO.getBookingsByUser(userId);
    }

    // =========================================
    // Get All Bookings
    // =========================================

    public List<Booking> getAllBookings() {

        return bookingDAO.getAllBookings();
    }

    // =========================================
    // Cancel Booking
    // =========================================

    public boolean cancelBooking(int bookingId)
            throws BookingNotFoundException,
            TrainNotFoundException {

        Booking booking = getBookingById(bookingId);

        // Already cancelled
        if ("CANCELLED".equalsIgnoreCase(
                booking.getStatus())) {

            return false;
        }

        // Release passenger seats
        int passengerCount = booking.getPassengers().size();

        boolean cancelled = bookingDAO.cancelBooking(bookingId);

        if (cancelled) {

            trainService.releaseSeats(
                    booking.getTrainId(),
                    passengerCount);
        }

        return cancelled;
    }

    // =========================================
    // Calculate Fare
    // =========================================

    public double calculateFare(int passengerCount) {

        if (passengerCount <= 0) {
            return 0;
        }

        return passengerCount * FARE_PER_PASSENGER;
    }

    // =========================================
    // Validate Passengers
    // =========================================

    private void validatePassengers(
            List<Passenger> passengers) {

        if (passengers == null ||
                passengers.isEmpty()) {

            throw new IllegalArgumentException(
                    "At least one passenger is required!");
        }

        if (passengers.size() > 6) {

            throw new IllegalArgumentException(
                    "Maximum 6 passengers allowed per booking!");
        }

        for (Passenger passenger : passengers) {

            if (passenger == null) {

                throw new IllegalArgumentException(
                        "Passenger cannot be null!");
            }

            if (passenger.getName() == null ||
                    passenger.getName().trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Passenger name cannot be empty!");
            }

            if (passenger.getAge() <= 0 ||
                    passenger.getAge() > 120) {

                throw new IllegalArgumentException(
                        "Invalid passenger age!");
            }

            if (passenger.getGender() == null ||
                    passenger.getGender().trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Passenger gender cannot be empty!");
            }
        }
    }
}