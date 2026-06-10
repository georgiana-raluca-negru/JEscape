package com.pao.escaperoom.commands;

import com.pao.escaperoom.model.GameSession;
import com.pao.escaperoom.model.PlayerProfile;
import com.pao.escaperoom.service.AuditService;

public class StatsCommand implements  Command{
    @Override
    public String execute(GameSession session, String[] args) {
        AuditService.getInstance().log("stats");
        PlayerProfile player = session.getPlayer();

        StringBuilder stats = new StringBuilder();
        stats.append("\n=========================================\n");
        stats.append("             IN GAME STATS               \n");
        stats.append("=========================================\n");

        stats.append("Player: ").append(player.getUsername());
        if (player.getTitle() != null) {
            stats.append(" [").append(player.getTitle().name()).append("]");
        }
        stats.append("\n");

        stats.append("Location: ").append(session.getCurrentLocation().getName()).append("\n");

        stats.append("Difficulty: ").append(session.getDifficulty().name()).append("\n");

        stats.append("Time Elapsed: ").append(session.getElapsedSeconds()).append(" sec\n");

        stats.append("=========================================");

        return stats.toString();
    }
}
