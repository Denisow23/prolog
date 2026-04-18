package com.example.lab2.io;

import com.example.lab2.model.Bus;
import com.example.lab2.model.CargoVan;
import com.example.lab2.model.ElectricBus;
import com.example.lab2.model.Location;
import com.example.lab2.model.TransportUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class CsvTransportDataLoader implements TransportDataLoader {
    private static final Logger LOGGER = Logger.getLogger(CsvTransportDataLoader.class.getName());

    @Override
    public List<TransportUnit> load(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<TransportUnit> result = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(",");
            if (parts.length < 3) {
                throw new IllegalArgumentException("Invalid CSV row: " + line);
            }

            String type = parts[0].trim().toLowerCase(Locale.ROOT);
            String id = parts[1].trim();
            int value = Integer.parseInt(parts[2].trim());

            TransportUnit unit = switch (type) {
                case "bus" -> new Bus(id, value);
                case "cargo_van" -> new CargoVan(id, value);
                case "electric_bus" -> {
                    if (parts.length != 6) {
                        throw new IllegalArgumentException("electric_bus requires 6 columns");
                    }
                    int battery = Integer.parseInt(parts[3].trim());
                    int x = Integer.parseInt(parts[4].trim());
                    int y = Integer.parseInt(parts[5].trim());
                    yield new ElectricBus(id, value, battery, new Location(x, y));
                }
                default -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
            };

            LOGGER.info(() -> "Loaded vehicle: " + unit.getId() + " type=" + type);
            result.add(unit);
        }

        return result;
    }
}
