package com.railway.dao;

import com.railway.model.Train;
import com.railway.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainDAO {

    // =========================
    // Add Train
    // =========================

    public boolean addTrain(Train train) {

        String sql = "INSERT INTO trains " +
                "(train_number, train_name, source, destination, " +
                "total_seats, available_seats) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, train.getTrainNumber());
            ps.setString(2, train.getTrainName());
            ps.setString(3, train.getSource());
            ps.setString(4, train.getDestination());
            ps.setInt(5, train.getTotalSeats());
            ps.setInt(6, train.getAvailableSeats());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error while adding train: "
                    + e.getMessage());

            return false;
        }
    }

    // =========================
    // Find Train By ID
    // =========================

    public Optional<Train> findById(int trainId) {

        String sql = "SELECT * FROM trains WHERE train_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, trainId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapTrain(rs));
            }

        } catch (SQLException e) {

            System.out.println("Error while finding train: "
                    + e.getMessage());
        }

        return Optional.empty();
    }

    // =========================
    // Find Train By Number
    // =========================

    public Optional<Train> findByTrainNumber(String trainNumber) {

        String sql = "SELECT * FROM trains WHERE train_number = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, trainNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapTrain(rs));
            }

        } catch (SQLException e) {

            System.out.println("Error while finding train: "
                    + e.getMessage());
        }

        return Optional.empty();
    }

    // =========================
    // Search Train
    // =========================

    public List<Train> searchTrains(String source, String destination) {

        List<Train> trains = new ArrayList<>();

        String sql = "SELECT * FROM trains " +
                "WHERE LOWER(source) = LOWER(?) " +
                "AND LOWER(destination) = LOWER(?)";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, source);
            ps.setString(2, destination);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                trains.add(mapTrain(rs));
            }

        } catch (SQLException e) {

            System.out.println("Error while searching trains: "
                    + e.getMessage());
        }

        return trains;
    }

    // =========================
    // Get All Trains
    // =========================

    public List<Train> getAllTrains() {

        List<Train> trains = new ArrayList<>();

        String sql = "SELECT * FROM trains";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                trains.add(mapTrain(rs));
            }

        } catch (SQLException e) {

            System.out.println("Error while fetching trains: "
                    + e.getMessage());
        }

        return trains;
    }

    // =========================
    // Update Train
    // =========================

    public boolean updateTrain(Train train) {

        String sql = "UPDATE trains SET " +
                "train_number = ?, " +
                "train_name = ?, " +
                "source = ?, " +
                "destination = ?, " +
                "total_seats = ?, " +
                "available_seats = ? " +
                "WHERE train_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, train.getTrainNumber());
            ps.setString(2, train.getTrainName());
            ps.setString(3, train.getSource());
            ps.setString(4, train.getDestination());
            ps.setInt(5, train.getTotalSeats());
            ps.setInt(6, train.getAvailableSeats());
            ps.setInt(7, train.getTrainId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error while updating train: "
                    + e.getMessage());

            return false;
        }
    }

    // =========================
    // Update Available Seats
    // =========================

    public boolean updateAvailableSeats(int trainId, int availableSeats) {

        String sql = "UPDATE trains SET available_seats = ? " +
                "WHERE train_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, availableSeats);
            ps.setInt(2, trainId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error while updating seats: "
                    + e.getMessage());

            return false;
        }
    }

    // =========================
    // Delete Train
    // =========================

    public boolean deleteTrain(int trainId) {

        String sql = "DELETE FROM trains WHERE train_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, trainId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error while deleting train: "
                    + e.getMessage());

            return false;
        }
    }

    // =========================
    // ResultSet → Train
    // =========================

    private Train mapTrain(ResultSet rs) throws SQLException {

        Train train = new Train();

        train.setTrainId(rs.getInt("train_id"));
        train.setTrainNumber(rs.getString("train_number"));
        train.setTrainName(rs.getString("train_name"));
        train.setSource(rs.getString("source"));
        train.setDestination(rs.getString("destination"));
        train.setTotalSeats(rs.getInt("total_seats"));
        train.setAvailableSeats(rs.getInt("available_seats"));

        return train;
    }
}