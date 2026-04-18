package com.example.lab2.strategy;

import com.example.lab2.model.DeliveryRequest;
import com.example.lab2.model.Route;
import com.example.lab2.model.TransportUnit;

public class EcoRouteStrategy implements RouteStrategy {
    @Override
    public Route buildRoute(TransportUnit unit, DeliveryRequest request) {
        int distance = request.distanceKm() + 2; // эко-маршрут: объезд загруженных участков
        return new Route("ECO", distance, unit.estimateEnergyForKm(distance) * 0.95);
    }
}
