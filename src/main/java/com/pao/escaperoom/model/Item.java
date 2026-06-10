package com.pao.escaperoom.model;

public abstract class Item extends  GameObject implements Storable{

    public Item(String name, String description) {
        super(name, description);
    }



}
