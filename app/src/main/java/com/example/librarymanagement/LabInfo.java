package com.example.librarymanagement;

public class LabInfo {
    public String labId, name, location, start_time, end_time, ownerUsername;
    public int floor, room, seats, fee;

    public LabInfo() {
        // Default constructor required for Firebase
    }

    public LabInfo(String labId, String name, String location, String start_time, String end_time,
                   int floor, int room, int seats, int fee, String ownerUsername) {
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


    public String getLabName() {
        return name;
    }
}
