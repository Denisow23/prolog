package com.example.lab2.strategy;

import com.example.lab2.model.Priority;

public class RouteStrategyFactory {
    public RouteStrategy create(Priority priority) {
        return switch (priority) {
            case STANDARD -> new ShortestRouteStrategy();
            case ECO -> new EcoRouteStrategy();
        };
    }
}
