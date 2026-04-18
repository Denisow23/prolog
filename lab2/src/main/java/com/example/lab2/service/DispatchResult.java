package com.example.lab2.service;

import com.example.lab2.model.Route;

public record DispatchResult(String requestId, String assignedUnitId, Route route) {
}
