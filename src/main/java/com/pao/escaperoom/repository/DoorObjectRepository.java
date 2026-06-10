package com.pao.escaperoom.repository;

import com.pao.escaperoom.db.DatabaseConnection;
import com.pao.escaperoom.model.DoorObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoorObjectRepository {
    private static DoorObjectRepository instance;

    private DoorObjectRepository() {}

    public static DoorObjectRepository getInstance() {
        if (instance == null) {
            instance = new DoorObjectRepository();
        }
        return instance;
    }

    public void save(DoorObject door, int locationId) throws SQLException {
        String sql = "INSERT INTO door_objects (name, description, is_locked, required_item_name, destination, is_final_exit, location_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, door.getName());
            stmt.setString(2, door.getDescription());
            stmt.setBoolean(3, door.isLocked());
            stmt.setString(4, door.getRequiredItemName());
            stmt.setString(5, door.getDestination());
            stmt.setBoolean(6, door.isFinalExit());
            stmt.setInt(7, locationId);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) door.setId(keys.getInt(1));
            }
        }
    }

    public List<DoorObject> findByLocationId(int locationId) throws SQLException {
        String sql = "SELECT * FROM door_objects WHERE location_id = ?";
        List<DoorObject> doors = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) doors.add(mapRow(rs));
            }
        }
        return doors;
    }

    public Optional<DoorObject> findById(int id) throws SQLException {
        String sql = "SELECT * FROM door_objects WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public List<DoorObject> findAll() throws SQLException {
        String sql = "SELECT * FROM door_objects";
        List<DoorObject> doors = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) doors.add(mapRow(rs));
        }
        return doors;
    }

    public void update(DoorObject door) throws SQLException {
        String sql = "UPDATE door_objects SET name = ?, description = ?, is_locked = ?, required_item_name = ?, destination = ?, is_final_exit = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, door.getName());
            stmt.setString(2, door.getDescription());
            stmt.setBoolean(3, door.isLocked());
            stmt.setString(4, door.getRequiredItemName());
            stmt.setString(5, door.getDestination());
            stmt.setBoolean(6, door.isFinalExit());
            stmt.setInt(7, door.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM door_objects WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private DoorObject mapRow(ResultSet rs) throws SQLException {
        DoorObject door = new DoorObject(
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("is_locked"),
                rs.getString("required_item_name"),
                rs.getString("destination"),
                rs.getBoolean("is_final_exit")
        );
        door.setId(rs.getInt("id"));
        return door;
    }
}
