package com.pao.escaperoom.repository;

import com.pao.escaperoom.db.DatabaseConnection;
import com.pao.escaperoom.model.Difficulty;
import com.pao.escaperoom.model.GameResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameResultRepository implements Repository<GameResult> {
    private static GameResultRepository instance;

    private static final String SELECT_WITH_USERNAME =
            "SELECT gr.*, p.username FROM game_results gr JOIN players p ON gr.player_id = p.id";

    private GameResultRepository() {}

    public static GameResultRepository getInstance() {
        if (instance == null) {
            instance = new GameResultRepository();
        }
        return instance;
    }

    @Override
    public void save(GameResult result) throws SQLException {
        String sql = "INSERT INTO game_results (player_id, map_name, difficulty, is_win, time_taken_seconds) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, result.getPlayerId());
            stmt.setString(2, result.getMapName());
            stmt.setString(3, result.getDifficulty().name());
            stmt.setBoolean(4, result.isWin());
            stmt.setLong(5, result.getTimeTakenSeconds());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) result.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public Optional<GameResult> findById(int id) throws SQLException {
        String sql = SELECT_WITH_USERNAME + " WHERE gr.id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public List<GameResult> findByPlayerId(int playerId) throws SQLException {
        String sql = SELECT_WITH_USERNAME + " WHERE gr.player_id = ?";
        List<GameResult> results = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        }
        return results;
    }

    @Override
    public List<GameResult> findAll() throws SQLException {
        List<GameResult> results = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(SELECT_WITH_USERNAME);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) results.add(mapRow(rs));
        }
        return results;
    }

    @Override
    public void update(GameResult result) throws SQLException {
        String sql = "UPDATE game_results SET map_name = ?, difficulty = ?, is_win = ?, time_taken_seconds = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, result.getMapName());
            stmt.setString(2, result.getDifficulty().name());
            stmt.setBoolean(3, result.isWin());
            stmt.setLong(4, result.getTimeTakenSeconds());
            stmt.setInt(5, result.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM game_results WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private GameResult mapRow(ResultSet rs) throws SQLException {
        GameResult result = new GameResult(
                rs.getString("username"),
                rs.getString("map_name"),
                Difficulty.valueOf(rs.getString("difficulty")),
                rs.getBoolean("is_win"),
                rs.getLong("time_taken_seconds")
        );
        result.setId(rs.getInt("id"));
        result.setPlayerId(rs.getInt("player_id"));
        return result;
    }
}
