package com.pao.escaperoom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// o harta se naste din a avea deja niste locatii in ea ca atfel nu ar avea sens
// totusi cred ca ar fi mai ok sa o fac pe asta clasa finala?

public class GameMap {
    private final String name;
    private final String description;

    private List<Location> locations;
    private Location startingLocation;

    public GameMap(String name, String description) {
        this.name = name;
        this.description = description;

        this.locations = new ArrayList<>();
        this.startingLocation = null;
    }

    // daca veau sa adaug o locatie in harta
    public void addLocation(Location location){
        this.locations.add(location);
    }

    public void setStartingLocation(Location location){
        this.startingLocation = location;
    }

    public Location getStartingLocation() {
        return startingLocation;
    }
}
