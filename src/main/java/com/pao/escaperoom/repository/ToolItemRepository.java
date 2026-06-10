package com.pao.escaperoom.repository;

import com.pao.escaperoom.db.DatabaseConnection;
import com.pao.escaperoom.model.ToolItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolItemRepository {
    private static ToolItemRepository instance;

    private ToolItemRepository() {}

    public static ToolItemRepository getInstance() {
        if (instance == null) {
            instance = new ToolItemRepository();
        }
        return instance;
    }

    public void saveToLocation(ToolItem item, int locationId) throws SQLException {
        save(item, locationId, null);
    }

    public void saveToContainer(ToolItem item, int containerId) throws SQLException {
        save(item, null, containerId);
    }

    private void save(ToolItem item, Integer locationId, Integer containerId) throws SQLException {
        String sql = "INSERT INTO tool_items (name, description, is_consumed_on_use, location_id, container_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setBoolean(3, item.isConsumedOnUse());
            if (locationId != null) stmt.setInt(4, locationId); else stmt.setNull(4, Types.INTEGER);
            if (containerId != null) stmt.setInt(5, containerId); else stmt.setNull(5, Types.INTEGER);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) item.setId(keys.getInt(1));
            }
        }
    }

    public List<ToolItem> findByLocationId(int locationId) throws SQLException {
        String sql = "SELECT * FROM tool_items WHERE location_id = ?";
        return findBy(sql, locationId);
    }

    public List<ToolItem> findByContainerId(int containerId) throws SQLException {
        String sql = "SELECT * FROM tool_items WHERE container_id = ?";
        return findBy(sql, containerId);
    }

    private List<ToolItem> findBy(String sql, int id) throws SQLException {
        List<ToolItem> items = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) items.add(mapRow(rs));
            }
        }
        return items;
    }

    public Optional<ToolItem> findById(int id) throws SQLException {
        String sql = "SELECT * FROM tool_items WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public List<ToolItem> findAll() throws SQLException {
        String sql = "SELECT * FROM tool_items";
        List<ToolItem> items = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) items.add(mapRow(rs));
        }
        return items;
    }

    public void update(ToolItem item) throws SQLException {
        String sql = "UPDATE tool_items SET name = ?, description = ?, is_consumed_on_use = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setBoolean(3, item.isConsumedOnUse());
            stmt.setInt(4, item.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM tool_items WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private ToolItem mapRow(ResultSet rs) throws SQLException {
        ToolItem item = new ToolItem(
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("is_consumed_on_use")
        );
        item.setId(rs.getInt("id"));
        return item;
    }
}
