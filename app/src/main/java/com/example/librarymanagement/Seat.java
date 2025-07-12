package com.example.librarymanagement;

public class Seat {
    private int seatNumber;
    private boolean isBooked;

    public Seat() {
        // Required for Firebase
    }

    public Seat(int seatNumber, boolean isBooked) {
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }
}
