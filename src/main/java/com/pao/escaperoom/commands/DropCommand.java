package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameSession;
import com.pao.escaperoom.model.Item;
import com.pao.escaperoom.model.Location;
import com.pao.escaperoom.service.AuditService;

public class DropCommand implements Command{
    public String execute(GameSession session, String[] args){
        AuditService.getInstance().log("drop");
        if(args.length == 0){
            return "Drop what? Please specify an object name.";
        }

        String objectName = String.join(" ", args);
        Location currentLocation = session.getCurrentLocation();
        Item itemToDrop = session.findItemByName(objectName);

        if(itemToDrop == null){
            return "There is no item named " + objectName + " in your inventory.";
        }
        currentLocation.addObject(itemToDrop);
        session.removeItemFromInventory(itemToDrop);

        return "You dropped " + itemToDrop.getName() + " on the floor.";
    }
}
