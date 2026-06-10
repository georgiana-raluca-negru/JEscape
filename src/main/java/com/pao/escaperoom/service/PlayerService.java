package com.pao.escaperoom.service;

import com.pao.escaperoom.exception.EmailTakenException;
import com.pao.escaperoom.exception.UsernameTakenException;
import com.pao.escaperoom.model.PlayerProfile;
import com.pao.escaperoom.model.PlayerTitle;
import com.pao.escaperoom.repository.GameResultRepository;
import com.pao.escaperoom.repository.PlayerRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerService {
    private static PlayerService instance;
    private final Map<String, PlayerProfile> playersByUsername;
    private final Map<String, PlayerProfile> playersByEmail;

    private PlayerService() {
        this.playersByUsername = new HashMap<>();
        this.playersByEmail = new HashMap<>();
        loadFromDatabase();
    }

    public static synchronized PlayerService getInstance() {
        if (instance == null) {
            instance = new PlayerService();
        }
        return instance;
    }

    private void loadFromDatabase() {
        try {
            List<PlayerProfile> players = PlayerRepository.getInstance().findAll();
            if (players.isEmpty()) {
                seedAdmin();
            } else {
                for (PlayerProfile player : players) {
                    playersByUsername.put(player.getUsername(), player);
                    playersByEmail.put(player.getEmail(), player);
                    GameResultRepository.getInstance()
                            .findByPlayerId(player.getId())
                            .forEach(player::addGameResult);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to load players from database: " + e.getMessage());
        }
    }

    private void seedAdmin() throws SQLException {
        PlayerProfile admin = new PlayerProfile("admin", "admin@escaperoom.com");
        admin.setTitle(PlayerTitle.VETERAN);
        PlayerRepository.getInstance().save(admin);
        playersByUsername.put(admin.getUsername(), admin);
        playersByEmail.put(admin.getEmail(), admin);
    }

    public boolean addPlayer(PlayerProfile player) throws UsernameTakenException, EmailTakenException {
        if (player == null || player.getUsername() == null || player.getEmail() == null) {
            return false;
        }
        if (playersByUsername.containsKey(player.getUsername())) {
            throw new UsernameTakenException(player.getUsername());
        }
        if (playersByEmail.containsKey(player.getEmail())) {
            throw new EmailTakenException(player.getEmail());
        }

        try {
            PlayerRepository.getInstance().save(player);
        } catch (SQLException e) {
            System.err.println("Failed to save player to database: " + e.getMessage());
            return false;
        }

        playersByUsername.put(player.getUsername(), player);
        playersByEmail.put(player.getEmail(), player);
        return true;
    }

    public PlayerProfile findPlayer(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) return null;
        return identifier.contains("@")
                ? playersByEmail.get(identifier)
                : playersByUsername.get(identifier);
    }

    public boolean updatePlayerTitle(String identifier, PlayerTitle newTitle) {
        PlayerProfile player = findPlayer(identifier);
        if (player == null || newTitle == null) return false;

        player.setTitle(newTitle);
        try {
            PlayerRepository.getInstance().update(player);
        } catch (SQLException e) {
            System.err.println("Failed to update player title in database: " + e.getMessage());
        }
        return true;
    }

    public boolean deletePlayer(String identifier) {
        PlayerProfile player = findPlayer(identifier);
        if (player == null) return false;

        try {
            PlayerRepository.getInstance().delete(player.getId());
        } catch (SQLException e) {
            System.err.println("Failed to delete player from database: " + e.getMessage());
            return false;
        }

        playersByUsername.remove(player.getUsername());
        playersByEmail.remove(player.getEmail());
        return true;
    }
}
