package com.example.lab2;

import com.example.lab2.model.CargoVan;
import com.example.lab2.model.DeliveryRequest;
import com.example.lab2.model.ElectricBus;
import com.example.lab2.model.Location;
import com.example.lab2.model.Priority;
import com.example.lab2.service.DispatchResult;
import com.example.lab2.service.DispatchService;
import com.example.lab2.service.Fleet;
import com.example.lab2.strategy.RouteStrategyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DispatchServiceTest {

    @Test
    void shouldUseEcoStrategyForEcoPriority_positive() {
        Fleet fleet = new Fleet();
        fleet.addUnit(new ElectricBus("EB-1", 60, 300, new Location(0, 0)));

        DispatchService service = new DispatchService(fleet, new RouteStrategyFactory());
        DeliveryRequest request = new DeliveryRequest("R-1", new Location(0, 0), new Location(10, 0), 200, 20, Priority.ECO);

        DispatchResult result = service.dispatch(request);
        assertEquals("ECO", result.route().mode());
        assertEquals("EB-1", result.assignedUnitId());
    }

    @Test
    void shouldFailWhenNoUnitCanServe_negative() {
        Fleet fleet = new Fleet();
        fleet.addUnit(new CargoVan("V-1", 300));

        DispatchService service = new DispatchService(fleet, new RouteStrategyFactory());
        DeliveryRequest heavyRequest = new DeliveryRequest("R-2", new Location(0, 0), new Location(3, 3), 5000, 0, Priority.STANDARD);

        assertThrows(IllegalStateException.class, () -> service.dispatch(heavyRequest));
    }

    @Test
    void shouldHandleBoundaryPassengers_boundary() {
        Fleet fleet = new Fleet();
        fleet.addUnit(new ElectricBus("EB-2", 120, 350, new Location(0, 0)));

        DispatchService service = new DispatchService(fleet, new RouteStrategyFactory());
        DeliveryRequest request = new DeliveryRequest("R-3", new Location(1, 1), new Location(2, 2), 0, 120, Priority.STANDARD);

        DispatchResult result = service.dispatch(request);
        assertEquals("SHORTEST", result.route().mode());
    }
}
