package com.railway.model;

import java.time.LocalDate;
import java.util.List;

public class Booking {

    private int bookingId;
    private int userId;
    private int trainId;
    private String pnr;
    private LocalDate journeyDate;
    private List<Passenger> passengers;
    private double totalFare;
    private String status;

    public Booking() {
    }

    public Booking(int bookingId, int userId, int trainId,
            String pnr, LocalDate journeyDate,
            List<Passenger> passengers,
            double totalFare, String status) {

        this.bookingId = bookingId;
        this.userId = userId;
        this.trainId = trainId;
        this.pnr = pnr;
        this.journeyDate = journeyDate;
        this.passengers = passengers;
        this.totalFare = totalFare;
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public LocalDate getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(LocalDate journeyDate) {
        this.journeyDate = journeyDate;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", userId=" + userId +
                ", trainId=" + trainId +
                ", pnr='" + pnr + '\'' +
                ", journeyDate=" + journeyDate +
                ", totalFare=" + totalFare +
                ", status='" + status + '\'' +
                '}';
    }
}