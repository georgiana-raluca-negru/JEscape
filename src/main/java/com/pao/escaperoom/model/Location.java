package com.pao.escaperoom.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void addObject(GameObject object){
        this.visibleObjects.add(object);
    }

    public void removeObject(GameObject object){
        this.visibleObjects.remove(object);
    }

    public void addExit(Direction direction, Location destination){
        this.exits.put(direction, destination);
    }

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
        return new ArrayList<>(this.visibleObjects);
    }

    public GameObject findObjectByName(String objectName){
        if(objectName == null || visibleObjects == null){
            return null;
        }

        for(GameObject object : visibleObjects){
            if(object.getName().equalsIgnoreCase(objectName.trim())){
                return object;
            }
        }

        return null;
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
