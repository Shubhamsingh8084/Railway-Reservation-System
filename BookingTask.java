package com.railway.thread;

import com.railway.model.Booking;
import com.railway.model.Passenger;
import com.railway.service.BookingService;

import java.time.LocalDate;
import java.util.List;

public class BookingTask implements Runnable {

    private final BookingService bookingService;

    private final int userId;
    private final int trainId;
    private final LocalDate journeyDate;
    private final List<Passenger> passengers;

    public BookingTask(int userId,
            int trainId,
            LocalDate journeyDate,
            List<Passenger> passengers) {

        this.bookingService = new BookingService();

        this.userId = userId;
        this.trainId = trainId;
        this.journeyDate = journeyDate;
        this.passengers = passengers;
    }

    @Override
    public void run() {

        try {

            System.out.println(
                    "Booking started by thread: "
                            + Thread.currentThread().getName());

            Booking booking = bookingService.createBooking(
                    userId,
                    trainId,
                    journeyDate,
                    passengers);

            System.out.println(
                    "Booking successful!");

            System.out.println(
                    "PNR: " + booking.getPnr());

            System.out.println(
                    "Total Fare: ₹"
                            + booking.getTotalFare());

            System.out.println(
                    "Thread completed: "
                            + Thread.currentThread().getName());

        } catch (Exception e) {

            System.out.println(
                    "Booking failed in thread "
                            + Thread.currentThread().getName()
                            + ": "
                            + e.getMessage());
        }
    }
}