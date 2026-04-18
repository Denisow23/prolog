package com.example.lab2.strategy;

import com.example.lab2.model.DeliveryRequest;
import com.example.lab2.model.Route;
import com.example.lab2.model.TransportUnit;

public class ShortestRouteStrategy implements RouteStrategy {
    @Override
    public Route buildRoute(TransportUnit unit, DeliveryRequest request) {
        int distance = request.distanceKm();
        return new Route("SHORTEST", distance, unit.estimateEnergyForKm(distance));
    }
}
