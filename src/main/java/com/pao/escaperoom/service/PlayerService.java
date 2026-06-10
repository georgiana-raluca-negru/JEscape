package com.pao.escaperoom.service;

import com.pao.escaperoom.exception.EmailTakenException;
import com.pao.escaperoom.exception.UsernameTakenException;
import com.pao.escaperoom.model.PlayerProfile;
import com.pao.escaperoom.model.PlayerTitle;

import java.util.HashMap;
import java.util.Map;

public class PlayerService {
    private static PlayerService instance;
    private final Map<String, PlayerProfile> playersByUsername;
    private final Map<String, PlayerProfile> playersByEmail;

    private PlayerService(){
        this.playersByUsername = new HashMap<>();
        this.playersByEmail = new HashMap<>();
        initializePlayers();
    }

    public static PlayerService getInstance(){
        if(instance == null){
            instance = new PlayerService();
        }
        return instance;
    }

    public boolean addPlayer(PlayerProfile player) throws UsernameTakenException, EmailTakenException {
        if (player == null || player.getUsername() == null || player.getEmail() == null){
            return false;
        }

        if(playersByUsername.containsKey(player.getUsername())){
            throw new UsernameTakenException(player.getUsername());
        }

        if(playersByEmail.containsKey(player.getEmail())){
            throw new EmailTakenException(player.getEmail());
        }

        playersByUsername.put(player.getUsername(), player);
        playersByEmail.put(player.getEmail(), player);

        return true;
    }

    public PlayerProfile findPlayer(String identifier){
        if(identifier == null || identifier.trim().isEmpty()){
            return null;
        }

        if(identifier.contains("@")){
            return playersByEmail.get(identifier);
        }
        else {
            return playersByUsername.get(identifier);
        }
    }

    public boolean updatePlayerTitle(String identifier, PlayerTitle newTitle){
        PlayerProfile player = findPlayer(identifier);

        if(player == null || newTitle == null){
            return  false;
        }

        player.setTitle(newTitle);
        return true;
    }

    public boolean deletePlayer(String identifier){
        PlayerProfile playerToRemove = findPlayer(identifier);

        if(playerToRemove == null){
            return false;
        }

        playersByUsername.remove(playerToRemove.getUsername());
        playersByEmail.remove(playerToRemove.getEmail());

        return true;
    }



    private void initializePlayers(){
        PlayerProfile player1 = new PlayerProfile("admin", "admin@escaperoom.com");
        PlayerProfile player2 = new PlayerProfile("player1", "player1@gmail.com");
        PlayerProfile player3 = new PlayerProfile("Escapist_Pro", "pro@yahoo.com");
        playersByUsername.put("admin", player1);
        playersByUsername.put("player1", player2);
        playersByUsername.put("Escapist_pro", player3);

        playersByEmail.put("admin@escaperoom.com", player1);
        playersByEmail.put("player1@gmail.com", player2);
        playersByEmail.put("pro@yahoo.com", player3);

    }

}
