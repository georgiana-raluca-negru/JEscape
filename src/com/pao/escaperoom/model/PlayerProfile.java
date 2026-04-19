package com.pao.escaperoom.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlayerProfile {
    private String username;
    private String email;
    private String passwordHash;
    private LocalDate registrationDate;

    private List<GameResult> gameHistory;

    public PlayerProfile(String username, String email, LocalDate registrationDate, String passwordHash) {
        this.username = username;
        this.email = email;
        this.registrationDate = registrationDate;
        this.passwordHash = passwordHash;

        this.gameHistory = new ArrayList<>();
    }


}
