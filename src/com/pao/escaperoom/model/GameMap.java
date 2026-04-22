package com.pao.escaperoom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public final class GameMap {
    private final String name;
    private final String description;

    private final List<Location> locations;
    private final Location startingLocation;

    public GameMap(String name, String description, List<Location> locations, Location startingLocation) {
        this.name = name;
        this.description = description;

        this.locations = new ArrayList<>(locations);
        this.startingLocation = startingLocation;
    }

    public Location getStartingLocation() {
        return startingLocation;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Location> getLocations() {
        return new ArrayList<>(this.locations);
    }

    @Override
    public String toString() {
        return "GameMap{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", numberOfLocations=" + locations.size() +
                ", startingLocation=" + startingLocation.getName() +
                '}';
    }

}
