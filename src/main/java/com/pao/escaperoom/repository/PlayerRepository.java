package com.pao.escaperoom.repository;

import com.pao.escaperoom.db.DatabaseConnection;
import com.pao.escaperoom.model.PlayerProfile;
import com.pao.escaperoom.model.PlayerTitle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerRepository implements Repository<PlayerProfile> {
    private static PlayerRepository instance;

    private PlayerRepository() {}

    public static PlayerRepository getInstance() {
        if (instance == null) {
            instance = new PlayerRepository();
        }
        return instance;
    }

    @Override
    public void save(PlayerProfile player) throws SQLException {
        String sql = "INSERT INTO players (username, email, title, registration_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, player.getUsername());
            stmt.setString(2, player.getEmail());
            stmt.setString(3, player.getTitle().name());
            stmt.setDate(4, Date.valueOf(player.getRegistrationDate()));
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) player.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public Optional<PlayerProfile> findById(int id) throws SQLException {
        String sql = "SELECT * FROM players WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<PlayerProfile> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM players WHERE username = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<PlayerProfile> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM players WHERE email = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<PlayerProfile> findAll() throws SQLException {
        String sql = "SELECT * FROM players";
        List<PlayerProfile> players = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) players.add(mapRow(rs));
        }
        return players;
    }

    @Override
    public void update(PlayerProfile player) throws SQLException {
        String sql = "UPDATE players SET title = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, player.getTitle().name());
            stmt.setInt(2, player.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM players WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private PlayerProfile mapRow(ResultSet rs) throws SQLException {
        PlayerProfile player = new PlayerProfile(
                rs.getString("username"),
                rs.getString("email"),
                rs.getDate("registration_date").toLocalDate()
        );
        player.setId(rs.getInt("id"));
        player.setTitle(PlayerTitle.valueOf(rs.getString("title")));
        return player;
    }
}
