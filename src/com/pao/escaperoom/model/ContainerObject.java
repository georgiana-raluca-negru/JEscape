package com.pao.escaperoom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ContainerObject extends  LockableObject implements Storable {
    private List<Storable> contents;

    public ContainerObject(String name, String description, boolean isLocked, String requiredItemName) {
        super(name, description, isLocked, requiredItemName);
        this.contents = new ArrayList<>();
    }

    @Override
    public String examine(){
        String message = description;
        if(isLocked){
            message += " It is firmly locked.";
        }
        else if(contents.isEmpty()){
            message += " It is open, but completely empty.";
        }
        else {
            message += "It is open. Inside you see: ";
            for(Storable object: contents){
                message += object.getName() + ", ";
            }
        }
        return message;
    }

    public void addInside(Storable object){
        contents.add(object);
    }


}
