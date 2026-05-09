package com.moonlight.petride.screens.rides.history_rides;

public class RideHistory {
    private String petName;
    private String date;
    private String time;
    private String location;
    private String status;
        private String walkerPhone;

    public RideHistory(String petName, String date, String time, String location, String status, String walkerPhone) {
        this.petName = petName;
        this.date = date;
        this.time = time;
        this.location = location;
        this.status = status;
        this.walkerPhone = walkerPhone;
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

    public String getWalkerPhone() {
        return walkerPhone;
    }
}
