package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.Direction;
import com.pao.escaperoom.model.GameSession;

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

        try{
            return session.movePlayer(dir);
        } catch (Exception e){
            return e.getMessage();
        }
    }
}
