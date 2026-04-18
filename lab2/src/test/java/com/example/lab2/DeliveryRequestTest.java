package com.example.lab2;

import com.example.lab2.model.DeliveryRequest;
import com.example.lab2.model.Location;
import com.example.lab2.model.Priority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeliveryRequestTest {

    @Test
    void shouldCreateValidRequest_positive() {
        DeliveryRequest request = new DeliveryRequest("REQ-1", new Location(0, 0), new Location(10, 5), 1000, 10, Priority.STANDARD);
        assertEquals(15, request.distanceKm());
    }

    @Test
    void shouldFailOnNegativeCargo_negative() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeliveryRequest("REQ-2", new Location(0, 0), new Location(1, 1), -1, 1, Priority.ECO));
    }

    @Test
    void shouldAllowBoundaryValues_boundary() {
        DeliveryRequest request = new DeliveryRequest("REQ-3", new Location(1000, 1000), new Location(1000, 1000), 10_000, 120, Priority.ECO);
        assertEquals(0, request.distanceKm());
    }
}
