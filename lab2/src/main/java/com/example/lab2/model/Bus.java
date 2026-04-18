package com.example.lab2.model;

public class Bus extends TransportUnit {
    public Bus(String id, int maxPassengers) {
        super(id, 500, maxPassengers);
    }

    @Override
    public double estimateEnergyForKm(int distanceKm) {
        if (distanceKm < 0) {
            throw new IllegalArgumentException("distanceKm must be >= 0");
        }
        return distanceKm * 0.35;
    }
}
