package com.pao.escaperoom.model;

import java.util.ArrayList;
import java.util.List;

public class ContainerObject extends  LockableObject implements Storable {
    private List<Storable> contents;

    public ContainerObject(String name, String description, boolean isLocked, String requiredItemName) {
        super(name, description, isLocked, requiredItemName);
        this.contents = new ArrayList<>();
    }

    @Override
    public String examine(){
        StringBuilder message = new StringBuilder(this.getDescription());
        message.append(" ");
        if(this.isLocked()){
            message.append("It is firmly locked.");
        }
        else if(contents.isEmpty()){
            message.append("It is open, but completely empty.");
        }
        else {
            message.append("It is open. Inside you see: ");
            for(Storable object: contents){
                message.append(object.getName()).append(", ");
            }
            message.setLength(message.length() -2);
        }
        return message.toString();
    }

    public void addInside(Storable object){
        contents.add(object);
    }


    @Override
    public String toString() {
        return "ContainerObject{" +
                "name='" + getName() + '\'' +
                ", isLocked=" + isLocked() +
                ", contentsCount=" + contents.size() +
                '}';
    }

    public List<Storable> getContents() {
        return contents;
    }

    public void setContents(List<Storable> contents) {
        this.contents = contents;
    }
}
