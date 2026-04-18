package com.example.lab2.model;

public class ElectricBus extends Bus implements ElectricPowered, GeoTrackable {
    private final int batteryKwh;
    private Location currentLocation;

    public ElectricBus(String id, int maxPassengers, int batteryKwh, Location startLocation) {
        super(id, maxPassengers);
        if (batteryKwh < 50 || batteryKwh > 700) {
            throw new IllegalArgumentException("batteryKwh must be in [50..700]");
        }
        this.batteryKwh = batteryKwh;
        this.currentLocation = startLocation;
    }

    @Override
    public int batteryKwh() {
        return batteryKwh;
    }

    @Override
    public Location currentLocation() {
        return currentLocation;
    }

    public void moveTo(Location location) {
        this.currentLocation = location;
    }

    @Override
    public double estimateEnergyForKm(int distanceKm) {
        if (distanceKm < 0) {
            throw new IllegalArgumentException("distanceKm must be >= 0");
        }
        return distanceKm * 0.22;
    }
}
