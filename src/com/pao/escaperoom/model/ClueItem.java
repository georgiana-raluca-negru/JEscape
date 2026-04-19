package com.pao.escaperoom.model;

public class ClueItem extends Item{
    private String hiddenMessage;

    public ClueItem(String name, String description, String hiddenMessage){
        super(name, description);
        this.hiddenMessage = hiddenMessage;
    }

    @Override
    public String examine(){
        return description + " Although, you notice something: " + hiddenMessage;
    }

    @Override
    public String useOn(GameObject target){
        return "You can't use " + name +  " like that. It's just a clue.";
    }
}
