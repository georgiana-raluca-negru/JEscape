package com.pao.escaperoom.service;

import com.pao.escaperoom.model.PlayerProfile;

import java.util.HashMap;
import java.util.Map;

public class PlayerService {
    private static PlayerService instance;

    // a map so taht i can check the existence of a plpayer fast
    private Map<String, PlayerProfile> players;

    private PlayerService(){
        this.players = new HashMap<>();
        initializePlayers();
    }

    public static PlayerService getInstance(){
        if(instance == null){
            instance = new PlayerService();
        }
        return instance;
    }

    public boolean addPlayer(PlayerProfile player){
        if (player == null || player.getUsername() == null){
            return false;
        }

        if(players.containsKey(player.getUsername())){
            return false;
        }

        players.put(player.getUsername(), player);
        return true;
    }

    public PlayerProfile findPlayerByName(String username){
        if(username == null){
            return null;
        }
        return players.get(username);
    }

    public boolean deletePlayer(String username){
        if(username == null){
            return false;
        }

        // returns null if there is not such object found
        PlayerProfile removedPlayer = players.remove(username);
        return removedPlayer != null;
    }

    private void initializePlayers(){
        // Adăugăm un admin
        players.put("admin", new PlayerProfile("admin", "admin@escaperoom.com"));
        players.put("player1", new PlayerProfile("player1", "player1@gmail.com"));
        players.put("Escapist_pro", new PlayerProfile("Escapist_Pro", "pro@yahoo.com"));
    }

}
