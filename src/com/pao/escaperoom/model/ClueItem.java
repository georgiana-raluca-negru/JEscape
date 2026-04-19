package com.pao.escaperoom.model;

public class ClueItem extends Item{
    private String hiddenMessage;

    public ClueItem(String name, String description, String hiddenMessage){
        super(name, description);
        this.hiddenMessage = hiddenMessage;
    }

    @Override
    public String examine(){
        return this.getDescription() + " Although, you notice something: " + hiddenMessage;
    }

    @Override
    public String useOn(GameObject target){
        return "You can't use " + this.getName() +  " like that. It's just a clue.";
    }

    public String getHiddenMessage() {
        return hiddenMessage;
    }

    public void setHiddenMessage(String hiddenMessage) {
        this.hiddenMessage = hiddenMessage;
    }
}
