package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameSession;

public class HelpCommand implements Command{
    @Override
    public String execute(GameSession session, String[] args){

        String sb = "=========================================================\n" +
                    "                    AVAILABLE COMMANDS                   \n" +
                    "=========================================================\n" +
                    "MOVEMENT\n" +
                    "  move/go [direction]           - Go north, south, east, or west.\n" +
                    "\nEXPLORATION\n" +
                    "  inspect room                  - Look around the current room.\n" +
                    "  inspect [item] from inventory - Look closely at an item you carry.\n" +
                    "  inspect [object] from room    - Look closely at an object in the room.\n" +
                    "\nINVENTORY & ITEMS\n" +
                    "  take [item]           - Pick up an item from the room.\n" +
                    "  drop [item]           - Drop an item from your inventory.\n" +
                    "  inventory (or inv)    - See what you are currently carrying.\n" +
                    "  use [item] on [target]- Use a tool/key on a specific object.\n" +
                    "\nSYSTEM\n" +
                    "  profile (or stats)    - View your player stats and time elapsed.\n" +
                    "  help                  - Show this message again.\n" +
                    "  quit                  - Surrender and exit the game.\n" +
                    "=========================================================";

        return sb;
    }
}
