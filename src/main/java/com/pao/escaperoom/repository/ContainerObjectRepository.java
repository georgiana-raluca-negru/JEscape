package com.pao.escaperoom.repository;

import com.pao.escaperoom.db.DatabaseConnection;
import com.pao.escaperoom.model.ContainerObject;
import com.pao.escaperoom.model.Storable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContainerObjectRepository {
    private static ContainerObjectRepository instance;

    private ContainerObjectRepository() {}

    public static ContainerObjectRepository getInstance() {
        if (instance == null) {
            instance = new ContainerObjectRepository();
        }
        return instance;
    }

    public void saveToLocation(ContainerObject container, int locationId) throws SQLException {
        save(container, locationId, null);
        saveContents(container);
    }

    public void saveToContainer(ContainerObject container, int parentContainerId) throws SQLException {
        save(container, null, parentContainerId);
        saveContents(container);
    }

    private void save(ContainerObject container, Integer locationId, Integer containerId) throws SQLException {
        String sql = "INSERT INTO container_objects (name, description, is_locked, required_item_name, location_id, container_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, container.getName());
            stmt.setString(2, container.getDescription());
            stmt.setBoolean(3, container.isLocked());
            stmt.setString(4, container.getRequiredItemName());
            if (locationId != null) stmt.setInt(5, locationId); else stmt.setNull(5, Types.INTEGER);
            if (containerId != null) stmt.setInt(6, containerId); else stmt.setNull(6, Types.INTEGER);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) container.setId(keys.getInt(1));
            }
        }
    }

    private void saveContents(ContainerObject container) throws SQLException {
        for (Storable storable : container.getContents()) {
            if (storable instanceof com.pao.escaperoom.model.ToolItem tool) {
                ToolItemRepository.getInstance().saveToContainer(tool, container.getId());
            } else if (storable instanceof com.pao.escaperoom.model.ClueItem clue) {
                ClueItemRepository.getInstance().saveToContainer(clue, container.getId());
            } else if (storable instanceof ContainerObject nested) {
                saveToContainer(nested, container.getId());
            }
        }
    }

    public List<ContainerObject> findByLocationId(int locationId) throws SQLException {
        String sql = "SELECT * FROM container_objects WHERE location_id = ?";
        return findBy(sql, locationId);
    }

    public List<ContainerObject> findByContainerId(int containerId) throws SQLException {
        String sql = "SELECT * FROM container_objects WHERE container_id = ?";
        return findBy(sql, containerId);
    }

    private List<ContainerObject> findBy(String sql, int id) throws SQLException {
        List<ContainerObject> containers = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ContainerObject container = mapRow(rs);
                    loadContents(container);
                    containers.add(container);
                }
            }
        }
        return containers;
    }

    public Optional<ContainerObject> findById(int id) throws SQLException {
        String sql = "SELECT * FROM container_objects WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ContainerObject container = mapRow(rs);
                    loadContents(container);
                    return Optional.of(container);
                }
            }
        }
        return Optional.empty();
    }

    public List<ContainerObject> findAll() throws SQLException {
        String sql = "SELECT * FROM container_objects";
        List<ContainerObject> containers = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ContainerObject container = mapRow(rs);
                loadContents(container);
                containers.add(container);
            }
        }
        return containers;
    }

    public void update(ContainerObject container) throws SQLException {
        String sql = "UPDATE container_objects SET name = ?, description = ?, is_locked = ?, required_item_name = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, container.getName());
            stmt.setString(2, container.getDescription());
            stmt.setBoolean(3, container.isLocked());
            stmt.setString(4, container.getRequiredItemName());
            stmt.setInt(5, container.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM container_objects WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private void loadContents(ContainerObject container) throws SQLException {
        List<Storable> contents = new ArrayList<>();
        contents.addAll(ToolItemRepository.getInstance().findByContainerId(container.getId()));
        contents.addAll(ClueItemRepository.getInstance().findByContainerId(container.getId()));
        contents.addAll(findByContainerId(container.getId()));
        contents.forEach(container::addInside);
    }

    private ContainerObject mapRow(ResultSet rs) throws SQLException {
        ContainerObject container = new ContainerObject(
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("is_locked"),
                rs.getString("required_item_name")
        );
        container.setId(rs.getInt("id"));
        return container;
    }
}
