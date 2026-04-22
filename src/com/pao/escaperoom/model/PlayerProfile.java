package com.pao.escaperoom.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlayerProfile {
    private final String username;
    private final String email;
    private PlayerTitle title;

    private final LocalDate registrationDate;
    private final List<GameResult> gameHistory;

    public PlayerProfile(String username, String email) {
        this.username = username;
        this.email = email;
        this.registrationDate = LocalDate.now();
        this.title = PlayerTitle.ROOKIE;

        this.gameHistory = new ArrayList<>();
    }

    public void addGameResult(GameResult result) {

        this.gameHistory.add(result);
    }

    public String getUsername() {

        return username;
    }

    public String getEmail() {

        return email;
    }

    public LocalDate getRegistrationDate() {

        return registrationDate;
    }

    public PlayerTitle getTitle() {
        return title;
    }

    public void setTitle(PlayerTitle title) {
        this.title = title;
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
