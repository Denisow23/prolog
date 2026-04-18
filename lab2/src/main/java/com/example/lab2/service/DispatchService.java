package com.example.lab2.service;

import com.example.lab2.model.DeliveryRequest;
import com.example.lab2.model.Route;
import com.example.lab2.model.TransportUnit;
import com.example.lab2.strategy.RouteStrategy;
import com.example.lab2.strategy.RouteStrategyFactory;

import java.util.Objects;
import java.util.logging.Logger;

public class DispatchService {
    private static final Logger LOGGER = Logger.getLogger(DispatchService.class.getName());

    private final Fleet fleet;
    private final RouteStrategyFactory routeStrategyFactory;

    public DispatchService(Fleet fleet, RouteStrategyFactory routeStrategyFactory) {
        this.fleet = Objects.requireNonNull(fleet);
        this.routeStrategyFactory = Objects.requireNonNull(routeStrategyFactory);
    }

    public DispatchResult dispatch(DeliveryRequest request) {
        TransportUnit unit = fleet.findBestUnitFor(request)
                .orElseThrow(() -> new IllegalStateException("No transport unit can serve request " + request.requestId()));

        RouteStrategy strategy = routeStrategyFactory.create(request.priority());
        Route route = strategy.buildRoute(unit, request);
        LOGGER.info(() -> "Assigned unit " + unit.getId() + " for request " + request.requestId()
                + " strategy=" + route.mode() + " distance=" + route.distanceKm());

        return new DispatchResult(request.requestId(), unit.getId(), route);
    }
}
