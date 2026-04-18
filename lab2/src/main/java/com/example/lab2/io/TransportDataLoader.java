package com.example.lab2.io;

import com.example.lab2.model.TransportUnit;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface TransportDataLoader {
    List<TransportUnit> load(Path path) throws IOException;
}
