package com.gpcompanion.race;

import com.gpcompanion.exceptions.*;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.util.List;

public class RaceLoader {
    public List<LapRecord> load(String filePath) throws RaceDataException {
        List<LapRecord> records = new ArrayList<>();

        Map<String, Driver> driversByName = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;

                String[] parts = line.split(",");
                if (parts.length != 7) {
                    throw new RaceDataException("Corrupted race data on line " + lineNumber + ": " + line);
                }

                int lapNumber = Integer.parseInt(parts[0].trim());
                String driverName = parts[1].trim();
                int carNumber = Integer.parseInt(parts[2].trim());
                String teamName = parts[3].trim();
                Color teamColor = Color.decode(parts[4].trim());
                double lapTime = Double.parseDouble(parts[5].trim());
                String tireCompound = parts[6].trim();

                Driver driver = driversByName.get(driverName);
                if (driver == null) {
                    driver = new Driver(driverName, carNumber, teamName, teamColor);
                    driversByName.put(driverName, driver);
                }

                records.add(new LapRecord(lapNumber, lapTime, tireCompound, driver));
            }
        } catch (IOException e) {
            throw new RaceDataException("Error reading race data: " + filePath, e);
        } catch (NumberFormatException e) {
            throw new RaceDataException("Numeric error in race data: " + filePath, e);
        }

        if (records.isEmpty()) {
            throw new RaceDataException("Race data empty: " + filePath);
        }

        return records;
    }
}