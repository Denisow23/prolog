package com.example.lab2.model;

import java.util.Objects;

public record Location(int x, int y) {
    public Location {
        if (x < 0 || x > 1000 || y < 0 || y > 1000) {
            throw new IllegalArgumentException("Coordinates must be in range [0..1000]");
        }
    }

    public int manhattanDistanceTo(Location other) {
        Objects.requireNonNull(other, "other location must not be null");
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}
