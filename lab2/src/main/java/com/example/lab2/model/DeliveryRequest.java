package com.example.lab2.model;

import java.util.Objects;

public record DeliveryRequest(
        String requestId,
        Location from,
        Location to,
        int cargoWeightKg,
        int passengers,
        Priority priority
) {
    public DeliveryRequest {
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("requestId is required");
        }
        Objects.requireNonNull(from, "from is required");
        Objects.requireNonNull(to, "to is required");
        Objects.requireNonNull(priority, "priority is required");
        if (cargoWeightKg < 0 || cargoWeightKg > 10_000) {
            throw new IllegalArgumentException("cargoWeightKg must be in [0..10000]");
        }
        if (passengers < 0 || passengers > 120) {
            throw new IllegalArgumentException("passengers must be in [0..120]");
        }
    }

    public int distanceKm() {
        return from.manhattanDistanceTo(to);
    }
}
