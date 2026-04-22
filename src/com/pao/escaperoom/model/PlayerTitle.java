package com.pao.escaperoom.model;

public enum PlayerTitle {
    ROOKIE("Rookie Escapist"),
    DETECTIVE("Amateur Detective"),
    MASTERMIND("Escape Mastemind"),
    LEGEND("Living Legend");

    private final String displayName;

    PlayerTitle(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return  displayName;
    }
}
