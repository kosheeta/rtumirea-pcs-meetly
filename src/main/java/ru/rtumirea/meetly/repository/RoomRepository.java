package ru.rtumirea.meetly.repository;

import ru.rtumirea.meetly.model.Room;
import ru.rtumirea.meetly.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {
    public Room save(Room room) {

        String sql = """
                INSERT INTO rooms (name, capacity, address)
                VALUES (?, ?, ?)
                RETURNING id
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, room.getName());
            statement.setInt(2, room.getCapacity());
            statement.setString(3, room.getAddress());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                room.setId(resultSet.getInt("id"));
            }

            return room;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Room> findAll() {

        String sql = "SELECT id, name, capacity, address FROM rooms ORDER BY id";

        List<Room> rooms = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                rooms.add(mapRow(resultSet));
            }

            return rooms;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Room> findByAddress(String address) {

        String sql = "SELECT id, name, capacity, address FROM rooms WHERE address ILIKE ? ORDER BY id";

        List<Room> rooms = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + address + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rooms.add(mapRow(resultSet));
                }
            }

            return rooms;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Room> findByMinCapacity(int minCapacity) {

        String sql = "SELECT id, name, capacity, address FROM rooms WHERE capacity >= ? ORDER BY id";

        List<Room> rooms = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, minCapacity);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rooms.add(mapRow(resultSet));
                }
            }

            return rooms;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Room findById(int id) {

        String sql = "SELECT id, name, capacity, address FROM rooms WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {

        String sql = "DELETE FROM rooms WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Room mapRow(ResultSet resultSet) throws SQLException {
        return new Room(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("capacity"),
                resultSet.getString("address")
        );
    }
}
