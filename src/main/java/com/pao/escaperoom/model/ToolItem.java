package com.pao.escaperoom.model;

public class ToolItem extends Item {
    private boolean isConsumedOnUse;

    public ToolItem(String name, String description, boolean isConsumedOnUse){
        super(name, description);
        this.isConsumedOnUse = isConsumedOnUse;
    }

    @Override
    public String examine(){
        String type = isConsumedOnUse ? "consumable." : "reusable.";
        return this.getDescription() + " It is " + type;
    }


    @Override
    public String toString() {
        return "ToolItem{" +
                "name='" + getName() + '\'' +
                ", isConsumedOnUse=" + isConsumedOnUse +
                '}';
    }

    public boolean isConsumedOnUse() {
        return isConsumedOnUse;
    }

    public void setConsumedOnUse(boolean consumedOnUse) {
        isConsumedOnUse = consumedOnUse;
    }
}
