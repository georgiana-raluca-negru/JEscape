package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameSession;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class CommandParser {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandParser(){
        commands.put("move", new MoveCommand());
        commands.put("go", new MoveCommand());
        commands.put("take", new TakeCommand());
        commands.put("use", new UseCommand());
        commands.put("inspect", new InspectCommand());
        commands.put("drop", new DropCommand());
        commands.put("help", new HelpCommand());
        commands.put("?", new HelpCommand());
    }

    public String parseAndExecute(GameSession session, String input){
        if(input == null || input.trim().isEmpty()){
            return "Please enter a command.";
        }

        String[] parts = input.trim().toLowerCase().split("\\s+");
        String commandName = parts[0];

        String[] args = new String[parts.length -1];
        System.arraycopy(parts, 1, args, 0, parts.length - 1);
        Command cmd = commands.get(commandName);
        if(cmd == null){
            return "Cannot recognise this command. Type 'help' for a list of all possible actions.";
        }

        return cmd.execute(session, args);
    }
}
