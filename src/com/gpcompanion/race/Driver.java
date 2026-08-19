package com.gpcompanion.race;

import java.awt.*;

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
}
