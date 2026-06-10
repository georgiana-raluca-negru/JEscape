package com.pao.escaperoom.service;

import com.pao.escaperoom.model.GameResult;
import com.pao.escaperoom.repository.GameResultRepository;

import java.sql.SQLException;
import java.util.TreeSet;

public class LeaderboardService {
    private static LeaderboardService instance;
    private final TreeSet<GameResult> globalLeaderboard;

    private LeaderboardService() {
        this.globalLeaderboard = new TreeSet<>();
        loadFromDatabase();
    }

    public static LeaderboardService getInstance() {
        if (instance == null) {
            instance = new LeaderboardService();
        }
        return instance;
    }

    private void loadFromDatabase() {
        try {
            GameResultRepository.getInstance().findAll().forEach(globalLeaderboard::add);
        } catch (SQLException e) {
            System.err.println("Failed to load leaderboard from database: " + e.getMessage());
        }
    }

    public void addResult(GameResult result) {
        if (result == null) return;
        globalLeaderboard.add(result);
        try {
            GameResultRepository.getInstance().save(result);
        } catch (SQLException e) {
            System.err.println("Failed to save game result to database: " + e.getMessage());
        }
    }

    public void displayTopResults(int n) {
        System.out.println("\n=========================================");
        System.out.println("             GLOBAL LEADERBOARD        ");
        System.out.println("=========================================");

        if (globalLeaderboard.isEmpty()) {
            System.out.println("No games played yet. Be the first to escape!");
            return;
        }

        int rank = 1;
        for (GameResult result : globalLeaderboard) {
            if (rank > n) break;
            System.out.println(rank + ". " + result.getPlayerName() +
                    " | Map: " + result.getMapName() +
                    " | Score: " + result.calculatePoints());
            rank++;
        }
    }
}
