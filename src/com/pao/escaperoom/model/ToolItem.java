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
        return description + " It is " + type;
    }

    @Override
    public String useOn(GameObject target){
        if ( target instanceof Interactable){
            Interactable interactableTarget = (Interactable) target;

            boolean isMatch = interactableTarget.interactWith(this);

            if (isMatch){
                return "You have succesfully used " + this.getName() +" on " + target.getName() +". Now it is unlocked.";
            }
            else{
                return "It doesn't fit or it is already unlocked.";
            }

        }
        return "You cannot use " + this.getName() + " on " + target.getName() + ".";
    }
}
