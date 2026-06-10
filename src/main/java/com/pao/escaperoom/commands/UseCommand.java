package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.*;

public class UseCommand implements Command{
    public String execute(GameSession session, String[] args) {
        if (args.length < 3) {
            return "Use what on what? (e.g., use key on door).";
        }

        int splitIndex = -1;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("on")) {
                splitIndex = i;
                break;
            }
        }

        if (splitIndex == -1 || splitIndex == 0 || splitIndex == args.length - 1) {
            return "Please use the format: use [item] on [target].";
        }

        String itemName = String.join(" ", java.util.Arrays.copyOfRange(args, 0, splitIndex));
        String targetName = String.join(" ", java.util.Arrays.copyOfRange(args, splitIndex + 1, args.length));

        Item inventoryItem = session.findItemByName(itemName);
        if (inventoryItem == null) {
            return "Your don't have " + itemName + " in your inventory.";
        }

        if (!(inventoryItem instanceof ToolItem tool)) {
            return "You can't use the " + inventoryItem.getName() + " like that.";
        }

        Location currentLocation = session.getCurrentLocation();
        GameObject targetObject = currentLocation.findObjectByName(targetName);

        if (targetObject == null) {
            return "There is no " + targetName + " in this room.";
        }
        if (!(targetObject instanceof Interactable target)) {
            return "You can't use the " + tool.getName() + " on the " + targetObject.getName() + ".";
        }

        boolean succes = target.interactWith(tool);
        if (succes) {
            if(tool.isConsumedOnUse()){
                session.removeItemFromInventory(tool);
                return "You have successfully used the " + tool.getName() + " on the " + targetObject.getName() + ". The " + tool.getName() + " breaks/is consumed in the process!";
            }
            return "You have successfully used the " + tool.getName() + " on the " + targetObject.getName() + ". It is now unlocked!";
        } else {
            return "You try to use the " + tool.getName() + " on the " + targetObject.getName() + ", but nothing happens. (Maybe it's the wrong tool, or it's already unlocked?).";
        }
    }
}
