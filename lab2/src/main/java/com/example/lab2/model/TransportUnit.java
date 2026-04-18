package com.example.lab2.model;

import java.util.Objects;

public abstract class TransportUnit {
    private final String id;
    private final int maxLoadKg;
    private final int maxPassengers;

    protected TransportUnit(String id, int maxLoadKg, int maxPassengers) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        if (maxLoadKg < 0 || maxLoadKg > 50_000) {
            throw new IllegalArgumentException("maxLoadKg must be in [0..50000]");
        }
        if (maxPassengers < 0 || maxPassengers > 200) {
            throw new IllegalArgumentException("maxPassengers must be in [0..200]");
        }
        this.id = id;
        this.maxLoadKg = maxLoadKg;
        this.maxPassengers = maxPassengers;
    }

    public String getId() {
        return id;
    }

    public int getMaxLoadKg() {
        return maxLoadKg;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public boolean canServe(DeliveryRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        return request.cargoWeightKg() <= maxLoadKg && request.passengers() <= maxPassengers;
    }

    public abstract double estimateEnergyForKm(int distanceKm);
}
