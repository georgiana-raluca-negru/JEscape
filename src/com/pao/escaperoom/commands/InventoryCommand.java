package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameSession;
import com.pao.escaperoom.model.Item;

public class InventoryCommand implements Command{
    @Override
    public String execute(GameSession session, String[] args){
        if(session.getInventory().isEmpty()){
            return "Your inventory is empty.";
        }

        StringBuilder sb = new StringBuilder("Your inventory: \n");
        for(Item item : session.getInventory()){
            sb.append("- ").append(item.getName()).append("\n");
        }
        return sb.toString().trim();
    }
}
