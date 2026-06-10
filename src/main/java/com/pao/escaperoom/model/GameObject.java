package com.pao.escaperoom.model;

public abstract class GameObject {
    protected int id;
    protected String name;
    protected String description;

    public GameObject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract String examine();
}
