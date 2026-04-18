package com.example.lab2;

import com.example.lab2.io.CsvTransportDataLoader;
import com.example.lab2.model.TransportUnit;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvTransportDataLoaderTest {

    @Test
    void shouldLoadUnits_positive() throws Exception {
        Path temp = Files.createTempFile("units", ".csv");
        Files.writeString(temp, "type,id,capacity,battery_kwh,x,y\n"
                + "bus,B-1,40,,,\n"
                + "cargo_van,V-1,900,,,\n");

        List<TransportUnit> units = new CsvTransportDataLoader().load(temp);
        assertEquals(2, units.size());
    }

    @Test
    void shouldFailOnUnknownType_negative() throws Exception {
        Path temp = Files.createTempFile("units-bad", ".csv");
        Files.writeString(temp, "type,id,capacity,battery_kwh,x,y\n"
                + "plane,P-1,40,,,\n");

        assertThrows(IllegalArgumentException.class, () -> new CsvTransportDataLoader().load(temp));
    }
}
