package com.pao.escaperoom.commands;

import com.pao.escaperoom.exception.InvalidMoveException;
import com.pao.escaperoom.model.*;

public class MoveCommand implements Command{

    @Override
    public String execute(GameSession session, String[] args){
        if(args.length == 0){
            return "Move where? (e.g., move north)";
        }

        Direction dir = Direction.fromString(args[0]);
        if(dir == null){
            return "Invalid direction! Use: north, south, east, west.";
        }

        Location currentLocation = session.getCurrentLocation();
        Location nextLocation = currentLocation.getExit(dir);


        if(nextLocation == null){
            return "You cannot go " + dir.name().toLowerCase() + ". There is no exit that way.";
        }

        for(GameObject object : currentLocation.getVisibleObjects()){
            if(object instanceof DoorObject door){
                if(door.getDestination().equalsIgnoreCase(nextLocation.getName())){
                    if(door.isLocked()){
                        return "You try to go " + dir.name().toLowerCase() + ", but the " + door.getName() + " is locked";
                    }
                    else {
                        if(door.isFinalExit()){
                            session.setIsWon(true);

                            return "You push open the " + door.getName() + " and step out into the light.\n" +
                                    "Fresh air hits your face. YOU HAVE ESCAPED!";
                        }
                    }
                }
            }
        }

        try{
            return session.movePlayer(dir);
        } catch (InvalidMoveException e){
            return e.getMessage();
        }
    }
}
