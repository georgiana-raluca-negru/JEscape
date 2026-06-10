package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.*;
import com.pao.escaperoom.service.AuditService;

public class TakeCommand implements Command {
    public String execute(GameSession session, String[] args){
        AuditService.getInstance().log("take");
        if(args.length == 0){
            return "Take what? Please specify an object name.";
        }

        String objectName = String.join(" ", args);
        Location currentLocation = session.getCurrentLocation();
        GameObject obj = currentLocation.findObjectByName(objectName);

        if(obj != null){
            if(obj instanceof Item itemToTake){
                currentLocation.removeObject(obj);
                session.addItemToInventory(itemToTake);

                return "You added " + obj.getName() + " to your inventory.";
            }
            else {
                return "The " + obj.getName()+ " is too bulky to carry in your inventory.";
            }
        }

        for(GameObject object : currentLocation.getVisibleObjects()){
            if(object instanceof ContainerObject container){
                if(!container.isLocked()){
                    for(Storable content : container.getContents()){
                        if(content instanceof Item itemToTake){
                            container.getContents().remove(content);
                            session.addItemToInventory(itemToTake);

                            return "You took " + itemToTake.getName() + " from the " + content.getName() + ".";
                        }
                        else {
                            return "The " + content.getName() + " is too bulky to carry.";
                        }
                    }
                }
            }
        }
        return  "There is no '" + objectName + "' here to take (or it might be locked inside something).";
    }
}
