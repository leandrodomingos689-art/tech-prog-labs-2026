package org.misis.tp.lab3.location;

import org.misis.tp.lab3.core.UnknownCodeError;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LocationRegistry {
    private final Map<String, Location> locations = new LinkedHashMap<>();

    public LocationRegistry(List<Location> locations) {
        for (Location location : locations) {
            this.locations.put(location.getCode(), location);
        }
    }

    public List<Location> all() {
        return List.copyOf(locations.values());
    }

    public Location get(String code) {
        Location location = locations.get(code);
        if (location == null) {
            throw new UnknownCodeError("Локация", code);
        }
        return location;
    }
}
