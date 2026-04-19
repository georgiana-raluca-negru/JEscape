package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameObject;
import com.pao.escaperoom.model.GameSession;
import com.pao.escaperoom.model.Item;
import com.pao.escaperoom.model.Location;

public class DropCommand implements Command{
    public String execute(GameSession session, String[] args){
        if(args.length == 0){
            return "Drop what? Please specify an object name.";
        }

        String objectName = String.join(" ", args);
        Location currentLocation = session.getCurrentLocation();
        Item object = session.findItemByName(objectName);

        if(object == null){
            return "There is no item named " + objectName + " in your inventory.";
        }
        currentLocation.addObject(object);
        session.removeItemFromInventory(object);

        return "You dropped " + object.getName() + " on the floor.";
    }
}
