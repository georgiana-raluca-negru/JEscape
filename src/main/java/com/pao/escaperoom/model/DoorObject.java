package com.pao.escaperoom.model;

public class DoorObject extends LockableObject{
    private String destination;
    private boolean isFinalExit;

    public DoorObject(String name, String description, boolean isLocked, String requiredItemName, String destination, boolean isFinalExit){
        super(name, description, isLocked, requiredItemName);
        this.destination = destination;
        this.isFinalExit = isFinalExit;
    }

    public String getDestination(){
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isFinalExit(){
        return isFinalExit;
    }

    @Override
    public String examine(){
        StringBuilder message = new StringBuilder(this.getDescription());
        message.append(" ");
        if(this.isLocked()){
            message.append("It is locked.");
        }
        else {
            message.append("It is unlocked and slightly open, leading to ").append(destination).append(".");
        }
        return message.toString();
    }

    @Override
    public String toString() {
        return "DoorObject{" +
                "name='" + getName() + '\'' +
                ", isLocked=" + isLocked() +
                ", destination='" + destination + '\'' +
                '}';
    }

}
