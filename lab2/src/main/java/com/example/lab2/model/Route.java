package com.example.lab2.model;

public record Route(String mode, int distanceKm, double expectedEnergy) {
    public Route {
        if (mode == null || mode.isBlank()) {
            throw new IllegalArgumentException("mode is required");
        }
        if (distanceKm < 0) {
            throw new IllegalArgumentException("distanceKm must be >= 0");
        }
        if (expectedEnergy < 0) {
            throw new IllegalArgumentException("expectedEnergy must be >= 0");
        }
    }
}
