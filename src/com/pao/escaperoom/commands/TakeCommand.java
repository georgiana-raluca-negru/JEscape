package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameObject;
import com.pao.escaperoom.model.GameSession;
import com.pao.escaperoom.model.Item;
import com.pao.escaperoom.model.Location;

public class TakeCommand implements Command {
    public String execute(GameSession session, String[] args){
        if(args.length == 0){
            return "Take what? Please specify an object name.";
        }

        String objectName = String.join(" ", args);
        Location currentLocation = session.getCurrentLocation();
        GameObject obj = currentLocation.findObjectByName(objectName);

        if(obj == null){
            return "There is no " + obj.getName() + " in this location.";
        }

        if(obj instanceof Item){
            Item itemToTake = (Item) obj;
            currentLocation.removeObject(obj);
            session.addItemToInventory(itemToTake);

            return "You added " + obj.getName() + " to your inventory.";
        }
        else {
            return "The " + obj.getName()+ " is too bulky to carry in your inventory.";
        }
    }
}
