package com.example.lab2;

import com.example.lab2.io.CsvTransportDataLoader;
import com.example.lab2.model.DeliveryRequest;
import com.example.lab2.model.Location;
import com.example.lab2.model.Priority;
import com.example.lab2.service.DispatchResult;
import com.example.lab2.service.DispatchService;
import com.example.lab2.service.Fleet;
import com.example.lab2.strategy.RouteStrategyFactory;

import java.nio.file.Path;
import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws Exception {
        Path csvPath = args.length > 0 ? Path.of(args[0]) : Path.of("src/main/resources/transport_units.csv");

        Fleet fleet = new Fleet();
        CsvTransportDataLoader loader = new CsvTransportDataLoader();
        loader.load(csvPath).forEach(fleet::addUnit);

        DispatchService service = new DispatchService(fleet, new RouteStrategyFactory());
        DeliveryRequest request = new DeliveryRequest(
                "R-001",
                new Location(10, 10),
                new Location(23, 25),
                300,
                35,
                Priority.ECO
        );

        DispatchResult result = service.dispatch(request);
        LOGGER.info(() -> "Dispatch result: unit=" + result.assignedUnitId() + ", mode=" + result.route().mode());
    }
}
