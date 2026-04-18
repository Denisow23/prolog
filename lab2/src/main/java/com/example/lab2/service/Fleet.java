package com.example.lab2.service;

import com.example.lab2.model.DeliveryRequest;
import com.example.lab2.model.TransportUnit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Fleet {
    private final List<TransportUnit> units = new ArrayList<>();

    public void addUnit(TransportUnit unit) {
        units.add(unit);
    }

    public List<TransportUnit> getUnits() {
        return List.copyOf(units);
    }

    public Optional<TransportUnit> findBestUnitFor(DeliveryRequest request) {
        return units.stream()
                .filter(unit -> unit.canServe(request))
                .max(Comparator.comparingInt(TransportUnit::getMaxLoadKg)
                        .thenComparingInt(TransportUnit::getMaxPassengers));
    }
}
