package com.gpcompanion.race;

import java.awt.*;
import java.util.Objects;

/// stores necessary driver information

public class Driver {
    private final String DriverName;
    private final int carNumber;
    private final String teamName;
    private final Color teamColor;

    public Driver(String driverName, int carNumber, String teamName, Color teamColor) {
        DriverName = driverName;
        this.carNumber = carNumber;
        this.teamName = teamName;
        this.teamColor = teamColor;
    }

    public String getName() {
        return this.DriverName;
    }

    public int getCarNumber() {
        return this.carNumber;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public Color getTeamColor() {
        return this.teamColor;
    }

    // RaceEngine uses Driver as a HashMap key (personalBests, standingMap) — without
    // this, lookups silently relied on RaceLoader never producing a duplicate instance
    // for the same driver. carNumber is the natural unique identifier.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver)) return false;
        Driver other = (Driver) o;
        return carNumber == other.carNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(carNumber);
    }
}
