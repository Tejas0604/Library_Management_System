package com.example.librarymanagement;

public class AdminFirstPageHelper {
    String labId, name, location, start_time, end_time, ownerUsername;
    int floor, room, seats, fee;

    public AdminFirstPageHelper() {
        // Default constructor required for Firebase
    }

    public AdminFirstPageHelper(String labId, String name, String location, String start_time,
                                String end_time, int floor, int room, int seats, int fee, String ownerUsername) {
        this.labId = labId;
        this.name = name;
        this.location = location;
        this.start_time = start_time;
        this.end_time = end_time;
        this.floor = floor;
        this.room = room;
        this.seats = seats;
        this.fee = fee;
        this.ownerUsername = ownerUsername;
    }

    public String getLabId() {
        return labId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}
