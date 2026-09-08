package com.railway.service;

import com.railway.exception.BookingNotFoundException;
import com.railway.exception.TrainNotFoundException;
import com.railway.model.Booking;
import com.railway.model.Passenger;
import com.railway.model.Ticket;
import com.railway.model.Train;

import java.util.List;
import java.util.stream.Collectors;

public class TicketService {

    private final BookingService bookingService;
    private final TrainService trainService;

    public TicketService() {
        this.bookingService = new BookingService();
        this.trainService = new TrainService();
    }

    // Generate tickets for all passengers
    public List<Ticket> generateTickets(String pnr)
            throws BookingNotFoundException,
            TrainNotFoundException {

        Booking booking = bookingService.getBookingByPNR(pnr);

        if (!"CONFIRMED".equalsIgnoreCase(
                booking.getStatus())) {

            throw new IllegalStateException(
                    "Ticket cannot be generated for cancelled booking!");
        }

        Train train = trainService.getTrainById(
                booking.getTrainId());

        List<Passenger> passengers = booking.getPassengers();

        int passengerCount = passengers.size();

        // Calculate first seat number
        int startSeat = train.getTotalSeats()
                - train.getAvailableSeats()
                - passengerCount + 1;

        return passengers.stream()
                .map(passenger -> {

                    int index = passengers.indexOf(passenger);

                    int seatNumber = startSeat + index;

                    double fare = bookingService.calculateFare(1);

                    return new Ticket(
                            booking.getPnr(),
                            train.getTrainNumber(),
                            train.getTrainName(),
                            train.getSource(),
                            train.getDestination(),
                            booking.getJourneyDate(),
                            passenger.getName(),
                            seatNumber,
                            fare,
                            booking.getStatus());
                })
                .collect(Collectors.toList());
    }

    // Print tickets
    public void printTickets(List<Ticket> tickets) {

        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No tickets available!");
            return;
        }

        System.out.println();
        System.out.println("========================================");
        System.out.println("          RAILWAY E-TICKET");
        System.out.println("========================================");

        for (Ticket ticket : tickets) {

            System.out.println("PNR           : "
                    + ticket.getPnr());

            System.out.println("Train Number  : "
                    + ticket.getTrainNumber());

            System.out.println("Train Name    : "
                    + ticket.getTrainName());

            System.out.println("From          : "
                    + ticket.getSource());

            System.out.println("To            : "
                    + ticket.getDestination());

            System.out.println("Journey Date  : "
                    + ticket.getJourneyDate());

            System.out.println("Passenger     : "
                    + ticket.getPassengerName());

            System.out.println("Seat Number   : "
                    + ticket.getSeatNumber());

            System.out.println("Fare          : ₹"
                    + ticket.getFare());

            System.out.println("Status        : "
                    + ticket.getStatus());

            System.out.println("----------------------------------------");
        }

        System.out.println("========================================");
    }
}