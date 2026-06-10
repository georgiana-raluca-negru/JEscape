package com.pao.escaperoom.repository;

import com.pao.escaperoom.db.DatabaseConnection;
import com.pao.escaperoom.model.*;

import java.sql.*;
import java.util.*;

public class GameMapRepository implements Repository<GameMap> {
    private static GameMapRepository instance;

    private GameMapRepository() {}

    public static GameMapRepository getInstance() {
        if (instance == null) {
            instance = new GameMapRepository();
        }
        return instance;
    }

    @Override
    public void save(GameMap map) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        try {
            // 1. Insert map row (without starting_location_id — circular FK)
            int mapId = insertMapRow(map, conn);
            map.setId(mapId);

            // 2. Save all locations
            for (Location loc : map.getLocations()) {
                insertLocation(loc, mapId, conn);
            }

            // 3. Set starting_location_id now that we have location IDs
            String updateSql = "UPDATE game_maps SET starting_location_id = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setInt(1, map.getStartingLocation().getId());
                stmt.setInt(2, mapId);
                stmt.executeUpdate();
            }

            // 4. Save exits
            for (Location loc : map.getLocations()) {
                for (Map.Entry<Direction, Location> exit : loc.getExits().entrySet()) {
                    insertExit(loc.getId(), exit.getKey(), exit.getValue().getId(), conn);
                }
            }

            // 5. Save game objects for each location
            for (Location loc : map.getLocations()) {
                for (GameObject obj : loc.getVisibleObjects()) {
                    saveGameObject(obj, loc.getId(), null);
                }
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public Optional<GameMap> findById(int id) throws SQLException {
        String sql = "SELECT * FROM game_maps WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(buildMap(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<GameMap> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM game_maps WHERE name = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(buildMap(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<GameMap> findAll() throws SQLException {
        String sql = "SELECT * FROM game_maps";
        List<GameMap> maps = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) maps.add(buildMap(rs));
        }
        return maps;
    }

    @Override
    public void update(GameMap map) throws SQLException {
        String sql = "UPDATE game_maps SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, map.getName());
            stmt.setString(2, map.getDescription());
            stmt.setInt(3, map.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        // ON DELETE CASCADE handles locations, exits, and game objects
        String sql = "DELETE FROM game_maps WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // --- private helpers ---

    private int insertMapRow(GameMap map, Connection conn) throws SQLException {
        String sql = "INSERT INTO game_maps (name, description) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, map.getName());
            stmt.setString(2, map.getDescription());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    private void insertLocation(Location loc, int mapId, Connection conn) throws SQLException {
        String sql = "INSERT INTO locations (map_id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, mapId);
            stmt.setString(2, loc.getName());
            stmt.setString(3, loc.getDescription());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                keys.next();
                loc.setId(keys.getInt(1));
                loc.setMapId(mapId);
            }
        }
    }

    private void insertExit(int fromId, Direction direction, int toId, Connection conn) throws SQLException {
        String sql = "INSERT INTO location_exits (from_location_id, direction, to_location_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fromId);
            stmt.setString(2, direction.name());
            stmt.setInt(3, toId);
            stmt.executeUpdate();
        }
    }

    private void saveGameObject(GameObject obj, int locationId, Integer containerId) throws SQLException {
        if (obj instanceof DoorObject door) {
            DoorObjectRepository.getInstance().save(door, locationId);
        } else if (obj instanceof ContainerObject container) {
            if (containerId == null) {
                ContainerObjectRepository.getInstance().saveToLocation(container, locationId);
            } else {
                ContainerObjectRepository.getInstance().saveToContainer(container, containerId);
            }
        } else if (obj instanceof ToolItem tool) {
            if (containerId == null) {
                ToolItemRepository.getInstance().saveToLocation(tool, locationId);
            } else {
                ToolItemRepository.getInstance().saveToContainer(tool, containerId);
            }
        } else if (obj instanceof ClueItem clue) {
            if (containerId == null) {
                ClueItemRepository.getInstance().saveToLocation(clue, locationId);
            } else {
                ClueItemRepository.getInstance().saveToContainer(clue, containerId);
            }
        }
    }

    private GameMap buildMap(ResultSet rs) throws SQLException {
        int mapId = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        int startingLocationId = rs.getInt("starting_location_id");

        // Load locations (rows only)
        Map<Integer, Location> locationById = loadLocations(mapId);

        // Wire up exits
        wireExits(locationById);

        // Load game objects into each location
        for (Location loc : locationById.values()) {
            loadObjectsIntoLocation(loc);
        }

        Location startingLocation = locationById.get(startingLocationId);
        return new GameMap(name, description, new ArrayList<>(locationById.values()), startingLocation);
    }

    private Map<Integer, Location> loadLocations(int mapId) throws SQLException {
        String sql = "SELECT * FROM locations WHERE map_id = ?";
        Map<Integer, Location> locationById = new LinkedHashMap<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, mapId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Location loc = new Location(rs.getString("name"), rs.getString("description"));
                    loc.setId(rs.getInt("id"));
                    loc.setMapId(mapId);
                    locationById.put(loc.getId(), loc);
                }
            }
        }
        return locationById;
    }

    private void wireExits(Map<Integer, Location> locationById) throws SQLException {
        String sql = "SELECT * FROM location_exits WHERE from_location_id = ?";
        for (Location loc : locationById.values()) {
            try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
                stmt.setInt(1, loc.getId());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Direction dir = Direction.valueOf(rs.getString("direction"));
                        Location dest = locationById.get(rs.getInt("to_location_id"));
                        loc.addExit(dir, dest);
                    }
                }
            }
        }
    }

    private void loadObjectsIntoLocation(Location loc) throws SQLException {
        DoorObjectRepository.getInstance().findByLocationId(loc.getId()).forEach(loc::addObject);
        ContainerObjectRepository.getInstance().findByLocationId(loc.getId()).forEach(loc::addObject);
        ToolItemRepository.getInstance().findByLocationId(loc.getId()).forEach(loc::addObject);
        ClueItemRepository.getInstance().findByLocationId(loc.getId()).forEach(loc::addObject);
    }
}
