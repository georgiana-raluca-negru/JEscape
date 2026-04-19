package com.pao.escaperoom.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// cu ce se naste o camera? cu nume, descreiere (maybe)
// iesirile si obiectele din camera sunt apoi adaugate in ea (logic nu?)
public class Location {
    private String name;
    private String description;

    private Map<Direction, Location> exits;
    private List<GameObject> visibleObjects;

    public Location(String name, String description) {
        this.name = name;
        this.description = description;

        this.exits = new HashMap<>();
        this.visibleObjects = new ArrayList<>();
    }

    // asta este ceva mai generic de tipul sa adaug obiecte in camera ca si developer
    public void addObject(GameObject object){
        this.visibleObjects.add(object);
    }

    // aici este ceva mai generic de tipul sa scog obiecte ca si developer din camera
    public void removeObject(GameObject object){
        this.visibleObjects.remove(object);
    }

    // adaug referinta unei locatii la o anumita directie
    public void addExit(Direction direction, Location destination){
        this.exits.put(direction, destination);
    }

    // referinta catre locatia de la o anumita directie
    public Location getExit(Direction direction){
        return this.exits.get(direction);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<Direction, Location> getExits() {
        return exits;
    }

    public List<GameObject> getVisibleObjects() {
        return visibleObjects;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", numberOfExits=" + exits.size() +
                ", visibleObjects=" + visibleObjects +
                '}';
    }
}
