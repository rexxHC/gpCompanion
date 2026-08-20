package com.gpcompanion.race;

import java.util.*;

public class RaceEngine {
    private int currentLap = 0;
    private int totalLaps = 0;
    private List<Standing> standings = new ArrayList<>();
    private Runnable listener;
    private final List<LapRecord> allLapRecords;
    private final Map<Driver, Double> personalBests = new HashMap<>();
    private double fastestLapOfRace = Double.MAX_VALUE;

    public RaceEngine(List<LapRecord> records) {
        this.allLapRecords = records;

        for (LapRecord r : records) {
            if (r.getLapNumber() > totalLaps) {
                totalLaps = r.getLapNumber();
            }
        }

        String[] tires = {"Soft", "Medium", "Hard"};

        int index = 0;
        for (LapRecord r : records) {
            if (r.getLapNumber() == 1) {
                Standing s = new Standing();
                s.setDriver(r.getDriver());
                s.setCurrentTire(tires[index % 3]);
                s.setPosition(index + 1);
                standings.add(s);
                index++;
            }
        }
    }

    public void setListener(Runnable listener) {
        this.listener = listener;
    }

    public boolean isFinished() {
        return currentLap >= totalLaps;
    }

    public void advanceLap() {
        if (currentLap < totalLaps) {
            currentLap++;

            List<LapRecord> currentLapRecords = new ArrayList<>();
            for (LapRecord r : allLapRecords) {
                if (r.getLapNumber() == currentLap) currentLapRecords.add(r);
            }

            Map<Driver, Standing> standingMap = new HashMap<>();
            for (Standing s : standings) {
                standingMap.put(s.getDriver(), s);
            }

            List<Standing> newStandings = new ArrayList<>();
            for (LapRecord r : currentLapRecords) {
                Standing s = standingMap.getOrDefault(r.getDriver(), new Standing());
                s.setDriver(r.getDriver());
                s.setTotalTime(s.getTotalTime() + r.getLapTime());
                s.setLastLapTime(r.getLapTime());

                double pb = personalBests.getOrDefault(r.getDriver(), Double.MAX_VALUE);
                if (r.getLapTime() < pb) {
                    personalBests.put(r.getDriver(), r.getLapTime());
                    s.setPersonalBest(true);
                } else {
                    s.setPersonalBest(false);
                }

                if (r.getLapTime() < fastestLapOfRace) {
                    fastestLapOfRace = r.getLapTime();
                }

                s.setFastestLap(r.getLapTime() <= fastestLapOfRace);

                newStandings.add(s);
            }

            Collections.sort(newStandings);

            for (int i = 0; i < newStandings.size(); i++) {
                Standing s = newStandings.get(i);
                int oldPosition = s.getPosition();
                int newPosition = i + 1;

                if (oldPosition <= 0) {
                    s.setPositionChange(Standing.PositionChange.NONE);
                } else if (newPosition < oldPosition) {
                    s.setPositionChange(Standing.PositionChange.GAINED);
                } else if (newPosition > oldPosition) {
                    s.setPositionChange(Standing.PositionChange.LOST);
                } else {
                    s.setPositionChange(Standing.PositionChange.NONE);
                }

                s.setPosition(newPosition);
                s.setGapToLeader(s.getTotalTime() - newStandings.get(0).getTotalTime());
                s.setIntervalToCarAhead(i == 0 ? 0 : s.getTotalTime() - newStandings.get(i - 1).getTotalTime());
            }

            standings = newStandings;
            if (listener != null) listener.run();
        }
    }

    public int getCurrentLap() {
        return currentLap;
    }

    public int getTotalLaps() {
        return totalLaps;
    }

    public List<Standing> getStandings() {
        return standings;
    }
}