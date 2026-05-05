package com.moonlight.petride.screens.rides.history_rides;

public class RideHistory {
    private String petName;
    private String date;
    private String time;
    private String location;
    private String status;

    public RideHistory(String petName, String date, String time, String location, String status) {
        this.petName = petName;
        this.date = date;
        this.time = time;
        this.location = location;
        this.status = status;
    }

    public String getPetName() {
        return petName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }
}
