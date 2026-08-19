package com.gpcompanion.race;

/// stores lap records

public class LapRecord {
    private final int lapNumber;
    private final double lapTime;
    private final String tireCompound;
    private final Driver driver;

    public LapRecord(int lapNumber, double lapTime, String tireCompound, Driver driver) {
        this.lapNumber = lapNumber;
        this.lapTime = lapTime;
        this.tireCompound = tireCompound;
        this.driver = driver;
    }

    public int getLapNumber() {
        return this.lapNumber;
    }

    public double getLapTime() {
        return this.lapTime;
    }

    public String getTireCompound() {
        return this.tireCompound;
    }

    public Driver getDriver() {
        return this.driver;
    }
}