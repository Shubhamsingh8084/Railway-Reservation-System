package com.railway.service;

import com.railway.dao.TrainDAO;
import com.railway.exception.SeatNotAvailableException;
import com.railway.exception.TrainNotFoundException;
import com.railway.model.Train;

import java.util.List;
import java.util.Optional;

public class TrainService {

    private final TrainDAO trainDAO;

    // Constructor
    public TrainService() {
        this.trainDAO = new TrainDAO();
    }

    // =========================================
    // Add Train
    // =========================================

    public boolean addTrain(Train train)
            throws TrainNotFoundException {

        validateTrain(train);

        Optional<Train> existingTrain = trainDAO.findByTrainNumber(
                train.getTrainNumber());

        if (existingTrain.isPresent()) {

            throw new TrainNotFoundException(
                    "Train number already exists!");
        }

        return trainDAO.addTrain(train);
    }

    // =========================================
    // Get Train By ID
    // =========================================

    public Train getTrainById(int trainId)
            throws TrainNotFoundException {

        if (trainId <= 0) {

            throw new TrainNotFoundException(
                    "Invalid train ID!");
        }

        Optional<Train> train = trainDAO.findById(trainId);

        if (train.isEmpty()) {

            throw new TrainNotFoundException(
                    "Train not found with ID: " + trainId);
        }

        return train.get();
    }

    // =========================================
    // Get Train By Number
    // =========================================

    public Train getTrainByNumber(String trainNumber)
            throws TrainNotFoundException {

        if (trainNumber == null ||
                trainNumber.trim().isEmpty()) {

            throw new TrainNotFoundException(
                    "Train number cannot be empty!");
        }

        Optional<Train> train = trainDAO.findByTrainNumber(trainNumber);

        if (train.isEmpty()) {

            throw new TrainNotFoundException(
                    "Train not found: " + trainNumber);
        }

        return train.get();
    }

    // =========================================
    // Search Trains
    // =========================================

    public List<Train> searchTrains(
            String source,
            String destination)
            throws TrainNotFoundException {

        if (source == null ||
                source.trim().isEmpty()) {

            throw new TrainNotFoundException(
                    "Source cannot be empty!");
        }

        if (destination == null ||
                destination.trim().isEmpty()) {

            throw new TrainNotFoundException(
                    "Destination cannot be empty!");
        }

        List<Train> trains = trainDAO.searchTrains(
                source,
                destination);

        if (trains.isEmpty()) {

            throw new TrainNotFoundException(
                    "No train available from "
                            + source + " to "
                            + destination);
        }

        return trains;
    }

    // =========================================
    // Get All Trains
    // =========================================

    public List<Train> getAllTrains() {

        return trainDAO.getAllTrains();
    }

    // =========================================
    // Update Train
    // =========================================

    public boolean updateTrain(Train train)
            throws TrainNotFoundException {

        if (train == null ||
                train.getTrainId() <= 0) {

            throw new TrainNotFoundException(
                    "Invalid train!");
        }

        // Check train exists
        getTrainById(train.getTrainId());

        validateTrain(train);

        return trainDAO.updateTrain(train);
    }

    // =========================================
    // Delete Train
    // =========================================

    public boolean deleteTrain(int trainId)
            throws TrainNotFoundException {

        // Check train exists
        getTrainById(trainId);

        return trainDAO.deleteTrain(trainId);
    }

    // =========================================
    // Check Seats
    // =========================================

    public void checkSeats(
            int trainId,
            int requiredSeats)
            throws TrainNotFoundException,
            SeatNotAvailableException {

        Train train = getTrainById(trainId);

        if (requiredSeats <= 0) {

            throw new SeatNotAvailableException(
                    "Number of seats must be greater than 0!");
        }

        if (train.getAvailableSeats() < requiredSeats) {

            throw new SeatNotAvailableException(
                    "Only "
                            + train.getAvailableSeats()
                            + " seats available!");
        }
    }

    // =========================================
    // Reserve Seats
    // =========================================

    public boolean reserveSeats(
            int trainId,
            int seats)
            throws TrainNotFoundException,
            SeatNotAvailableException {

        checkSeats(trainId, seats);

        Train train = getTrainById(trainId);

        int remainingSeats = train.getAvailableSeats() - seats;

        return trainDAO.updateAvailableSeats(
                trainId,
                remainingSeats);
    }

    // =========================================
    // Release Seats
    // =========================================

    public boolean releaseSeats(
            int trainId,
            int seats)
            throws TrainNotFoundException {

        Train train = getTrainById(trainId);

        if (seats <= 0) {
            return false;
        }

        int availableSeats = train.getAvailableSeats() + seats;

        if (availableSeats > train.getTotalSeats()) {

            availableSeats = train.getTotalSeats();
        }

        return trainDAO.updateAvailableSeats(
                trainId,
                availableSeats);
    }

    // =========================================
    // Validate Train
    // =========================================

    private void validateTrain(Train train)
            throws TrainNotFoundException {

        if (train == null) {

            throw new TrainNotFoundException(
                    "Train cannot be null!");
        }

        if (train.getTrainNumber() == null ||
                train.getTrainNumber().trim().isEmpty()) {

            throw new TrainNotFoundException(
                    "Train number cannot be empty!");
        }

        if (train.getTrainName() == null ||
                train.getTrainName().trim().isEmpty()) {

            throw new TrainNotFoundException(
                    "Train name cannot be empty!");
        }

        if (train.getSource() == null ||
                train.getSource().trim().isEmpty()) {

            throw new TrainNotFoundException(
                    "Source cannot be empty!");
        }

        if (train.getDestination() == null ||
                train.getDestination().trim().isEmpty()) {

            throw new TrainNotFoundException(
                    "Destination cannot be empty!");
        }

        if (train.getTotalSeats() <= 0) {

            throw new TrainNotFoundException(
                    "Total seats must be greater than 0!");
        }
    }
}