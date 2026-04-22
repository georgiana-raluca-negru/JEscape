package com.pao.escaperoom.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlayerProfile {
    private String username;
    private String email;
    private final LocalDate registrationDate;

    private List<GameResult> gameHistory;

    public PlayerProfile(String username, String email) {
        this.username = username;
        this.email = email;
        this.registrationDate = LocalDate.now();

        this.gameHistory = new ArrayList<>();
    }

    public void addGameResult(GameResult result) {

        this.gameHistory.add(result);
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public LocalDate getRegistrationDate() {

        return registrationDate;
    }

    public List<GameResult> getGameHistory() {
        return new ArrayList<>(this.gameHistory);
    }

    public void setGameHistory(List<GameResult> gameHistory) {
        this.gameHistory = gameHistory;
    }

    @Override
    public String toString() {
        return "PlayerProfile{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", gamesPlayed=" + gameHistory.size() +
                '}';
    }
}
