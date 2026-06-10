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
    public String getHiddenMessage() {
        return hiddenMessage;
    }

    public void setHiddenMessage(String hiddenMessage) {
        this.hiddenMessage = hiddenMessage;
    }
}
