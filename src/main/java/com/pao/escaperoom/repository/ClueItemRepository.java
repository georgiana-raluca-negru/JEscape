package com.pao.escaperoom.repository;

import com.pao.escaperoom.db.DatabaseConnection;
import com.pao.escaperoom.model.ClueItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClueItemRepository {
    private static ClueItemRepository instance;

    private ClueItemRepository() {}

    public static ClueItemRepository getInstance() {
        if (instance == null) {
            instance = new ClueItemRepository();
        }
        return instance;
    }

    public void saveToLocation(ClueItem item, int locationId) throws SQLException {
        save(item, locationId, null);
    }

    public void saveToContainer(ClueItem item, int containerId) throws SQLException {
        save(item, null, containerId);
    }

    private void save(ClueItem item, Integer locationId, Integer containerId) throws SQLException {
        String sql = "INSERT INTO clue_items (name, description, hidden_message, location_id, container_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setString(3, item.getHiddenMessage());
            if (locationId != null) stmt.setInt(4, locationId); else stmt.setNull(4, Types.INTEGER);
            if (containerId != null) stmt.setInt(5, containerId); else stmt.setNull(5, Types.INTEGER);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) item.setId(keys.getInt(1));
            }
        }
    }

    public List<ClueItem> findByLocationId(int locationId) throws SQLException {
        String sql = "SELECT * FROM clue_items WHERE location_id = ?";
        return findBy(sql, locationId);
    }

    public List<ClueItem> findByContainerId(int containerId) throws SQLException {
        String sql = "SELECT * FROM clue_items WHERE container_id = ?";
        return findBy(sql, containerId);
    }

    private List<ClueItem> findBy(String sql, int id) throws SQLException {
        List<ClueItem> items = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) items.add(mapRow(rs));
            }
        }
        return items;
    }

    public Optional<ClueItem> findById(int id) throws SQLException {
        String sql = "SELECT * FROM clue_items WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public List<ClueItem> findAll() throws SQLException {
        String sql = "SELECT * FROM clue_items";
        List<ClueItem> items = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) items.add(mapRow(rs));
        }
        return items;
    }

    public void update(ClueItem item) throws SQLException {
        String sql = "UPDATE clue_items SET name = ?, description = ?, hidden_message = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setString(3, item.getHiddenMessage());
            stmt.setInt(4, item.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM clue_items WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private ClueItem mapRow(ResultSet rs) throws SQLException {
        ClueItem item = new ClueItem(
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("hidden_message")
        );
        item.setId(rs.getInt("id"));
        return item;
    }
}
