package com.example.lab2.model;

public class CargoVan extends TransportUnit {
    public CargoVan(String id, int maxLoadKg) {
        super(id, maxLoadKg, 2);
    }

    @Override
    public double estimateEnergyForKm(int distanceKm) {
        if (distanceKm < 0) {
            throw new IllegalArgumentException("distanceKm must be >= 0");
        }
        return distanceKm * 0.42;
    }
}
