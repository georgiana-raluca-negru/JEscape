package com.pao.escaperoom.service;

public class PlayerService {
    private static PlayerService instance;

    private PlayerService(){

    }

    public static PlayerService getInstance(){
        if(instance == null){
            instance = new PlayerService();
        }
        return instance;
    }
}
