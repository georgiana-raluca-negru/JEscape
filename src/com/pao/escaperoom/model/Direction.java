package com.pao.escaperoom.model;

public enum Direction {
    NORTH("north", "n"),
    EAST("east", "e"),
    WEST("west", "w"),
    SOUTH("south", "s");

    private final String fullName;
    private final String  shortName;

    private Direction(String fullName, String shortName){
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public String getFullName(){
        return fullName;
    }

    public String getShortName(){
        return shortName;
    }

    // caz in care introduc ceva de genul nOrtH si imi va identiifica corect pentru ca aduc totoul la lowercase
    public static Direction fromString(String input){
        if(input == null){
            return null;
        }

        String lowerInput = input.trim().toLowerCase();
        for(Direction dir : Direction.values()){
            if(dir.fullName.equals(lowerInput) || dir.shortName.equals((lowerInput))){
                return dir;
            }
        }
        return null;
    }

}
