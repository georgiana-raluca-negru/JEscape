package com.pao.escaperoom.model;

public abstract class LockableObject extends GameObject implements Interactable{
    protected boolean isLocked;
    protected String requiredItemName;

    public LockableObject(String name, String description, boolean isLocked, String requiredItemName){
        super(name, description);
        this.isLocked = isLocked;
        this.requiredItemName = requiredItemName;
    }

    @Override
    public boolean interactWith(ToolItem tool){
        if(!isLocked){
            return false;
        }

        if(tool.getName().equalsIgnoreCase(requiredItemName)){
            this.isLocked = false;
            return true;
        }

        return false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getRequiredItemName() {
        return requiredItemName;
    }

    public void setRequiredItemName(String requiredItemName) {
        this.requiredItemName = requiredItemName;
    }
}
