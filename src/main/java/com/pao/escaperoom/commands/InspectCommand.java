package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameObject;
import com.pao.escaperoom.model.GameSession;
import com.pao.escaperoom.model.Item;
import com.pao.escaperoom.model.Location;
import com.pao.escaperoom.service.AuditService;

public class InspectCommand implements Command{
    public String execute(GameSession session, String[] args){
        AuditService.getInstance().log("inspect");
        if(args.length == 0){
            return "Inspect what? (Try: 'room', '[item] from inventory', or [object] from room').";
        }
        Location currentLocation = session.getCurrentLocation();
        String fullCommand = String.join(" ", args).toLowerCase();

        // inspect room
        if(fullCommand.equals("room")){
            StringBuilder roomDesc = new StringBuilder(currentLocation.getDescription());

            if(!currentLocation.getVisibleObjects().isEmpty()){
                roomDesc.append("\nLooking around, you see: ");
                for(GameObject object : currentLocation.getVisibleObjects()){
                    roomDesc.append(object.getName()).append(", ");
                }

                roomDesc.setLength(roomDesc.length() - 2);
                roomDesc.append(".");
            }
            return roomDesc.toString();
        }

        if(fullCommand.endsWith("from inventory")){
            String itemName = fullCommand.replace("from inventory", "").trim();

            if(itemName.isEmpty()){
                return "Specify an item to inspect from your inventory.";
            }

            Item inventoryItem = session.findItemByName(itemName);
            if(inventoryItem != null){
                return "(Inventory) " + inventoryItem.examine();
            }
            else {
                return "You don't have '" + itemName + "' in your inventory.";
            }
        }

        if(fullCommand.endsWith("from room")){
            String targetName = fullCommand.replace("from room", "").trim();

            if(targetName.isEmpty()){
                return "Specify an object to inspect from the room.";
            }

            GameObject roomObject = currentLocation.findObjectByName(targetName);
            if(roomObject != null){
                return "(Room) " + roomObject.examine();
            }
            else {
                return "There is no '" + targetName + "' in the room.";
            }
        }
        return "I don't understand. Use 'inspect room', 'inspect [object] from inventory', or 'inspect [object] from room'.";
    }
}
