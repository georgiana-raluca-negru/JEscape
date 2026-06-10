package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameSession;
import com.pao.escaperoom.model.Item;
import com.pao.escaperoom.service.AuditService;

public class InventoryCommand implements Command{
    @Override
    public String execute(GameSession session, String[] args){
        AuditService.getInstance().log("inventory");
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
