package com.railway.dao;

import com.railway.model.Booking;
import com.railway.model.Passenger;
import com.railway.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDAO {

    // =========================================
    // Create Booking
    // =========================================

    public boolean createBooking(Booking booking) {

        String bookingSql = "INSERT INTO bookings " +
                "(user_id, train_id, pnr, journey_date, total_fare, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String passengerSql = "INSERT INTO passengers " +
                "(booking_id, name, age, gender, seat_preference) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection con = null;

        try {
            con = DBConnection.getConnection();

            // Start transaction
            con.setAutoCommit(false);

            // -------------------------
            // Insert Booking
            // -------------------------

            int bookingId;

            try (PreparedStatement ps = con.prepareStatement(
                    bookingSql,
                    Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, booking.getUserId());
                ps.setInt(2, booking.getTrainId());
                ps.setString(3, booking.getPnr());
                ps.setDate(4,
                        Date.valueOf(booking.getJourneyDate()));
                ps.setDouble(5, booking.getTotalFare());
                ps.setString(6, booking.getStatus());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();

                if (!rs.next()) {
                    throw new SQLException(
                            "Booking ID could not be generated.");
                }

                bookingId = rs.getInt(1);
            }

            // -------------------------
            // Insert Passengers
            // -------------------------

            try (PreparedStatement ps = con.prepareStatement(passengerSql)) {

                for (Passenger passenger : booking.getPassengers()) {

                    ps.setInt(1, bookingId);
                    ps.setString(2, passenger.getName());
                    ps.setInt(3, passenger.getAge());
                    ps.setString(4, passenger.getGender());
                    ps.setString(5,
                            passenger.getSeatPreference());

                    ps.addBatch();
                }

                ps.executeBatch();
            }

            // Commit transaction
            con.commit();

            booking.setBookingId(bookingId);

            return true;

        } catch (SQLException e) {

            // Rollback if anything fails
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackException) {
                    System.out.println(
                            "Rollback failed: "
                                    + rollbackException.getMessage());
                }
            }

            System.out.println(
                    "Error while creating booking: "
                            + e.getMessage());

            return false;

        } finally {

            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error while closing connection: "
                                    + e.getMessage());
                }
            }
        }
    }

    // =========================================
    // Find Booking By ID
    // =========================================

    public Optional<Booking> findById(int bookingId) {

        String sql = "SELECT * FROM bookings WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Booking booking = mapBooking(rs);

                booking.setPassengers(
                        getPassengers(bookingId));

                return Optional.of(booking);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while finding booking: "
                            + e.getMessage());
        }

        return Optional.empty();
    }

    // =========================================
    // Find Booking By PNR
    // =========================================

    public Optional<Booking> findByPNR(String pnr) {

        String sql = "SELECT * FROM bookings WHERE pnr = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pnr);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Booking booking = mapBooking(rs);

                booking.setPassengers(
                        getPassengers(booking.getBookingId()));

                return Optional.of(booking);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while finding booking: "
                            + e.getMessage());
        }

        return Optional.empty();
    }

    // =========================================
    // Get User's Bookings
    // =========================================

    public List<Booking> getBookingsByUser(int userId) {

        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM bookings " +
                "WHERE user_id = ? " +
                "ORDER BY journey_date DESC";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Booking booking = mapBooking(rs);

                booking.setPassengers(
                        getPassengers(booking.getBookingId()));

                bookings.add(booking);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while fetching bookings: "
                            + e.getMessage());
        }

        return bookings;
    }

    // =========================================
    // Get All Bookings
    // =========================================

    public List<Booking> getAllBookings() {

        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM bookings";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Booking booking = mapBooking(rs);

                booking.setPassengers(
                        getPassengers(booking.getBookingId()));

                bookings.add(booking);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while fetching all bookings: "
                            + e.getMessage());
        }

        return bookings;
    }

    // =========================================
    // Cancel Booking
    // =========================================

    public boolean cancelBooking(int bookingId) {

        String sql = "UPDATE bookings " +
                "SET status = 'CANCELLED' " +
                "WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error while cancelling booking: "
                            + e.getMessage());

            return false;
        }
    }

    // =========================================
    // Get Passengers
    // =========================================

    private List<Passenger> getPassengers(int bookingId)
            throws SQLException {

        List<Passenger> passengers = new ArrayList<>();

        String sql = "SELECT * FROM passengers " +
                "WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                passengers.add(mapPassenger(rs));
            }
        }

        return passengers;
    }

    // =========================================
    // Map ResultSet → Booking
    // =========================================

    private Booking mapBooking(ResultSet rs)
            throws SQLException {

        Booking booking = new Booking();

        booking.setBookingId(
                rs.getInt("booking_id"));

        booking.setUserId(
                rs.getInt("user_id"));

        booking.setTrainId(
                rs.getInt("train_id"));

        booking.setPnr(
                rs.getString("pnr"));

        booking.setJourneyDate(
                rs.getDate("journey_date")
                        .toLocalDate());

        booking.setTotalFare(
                rs.getDouble("total_fare"));

        booking.setStatus(
                rs.getString("status"));

        return booking;
    }

    // =========================================
    // Map ResultSet → Passenger
    // =========================================

    private Passenger mapPassenger(ResultSet rs)
            throws SQLException {

        Passenger passenger = new Passenger();

        passenger.setPassengerId(
                rs.getInt("passenger_id"));

        passenger.setName(
                rs.getString("name"));

        passenger.setAge(
                rs.getInt("age"));

        passenger.setGender(
                rs.getString("gender"));

        passenger.setSeatPreference(
                rs.getString("seat_preference"));

        return passenger;
    }
}