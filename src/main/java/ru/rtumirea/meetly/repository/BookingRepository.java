package ru.rtumirea.meetly.repository;

import ru.rtumirea.meetly.model.Booking;
import ru.rtumirea.meetly.model.BookingStatus;
import ru.rtumirea.meetly.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository implements BaseRepository<Booking, Integer> {
    @Override
    public Booking save(Booking booking) {

        String sql = """
                INSERT INTO bookings (user_id, room_id, start_time, end_time, status)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, booking.getUserId());
            statement.setInt(2, booking.getRoomId());
            statement.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            statement.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            statement.setString(5, booking.getStatus().getDbValue());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                booking.setId(resultSet.getInt("id"));
            }

            return booking;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> findAll() {

        String sql = "SELECT id, user_id, room_id, start_time, end_time, status FROM bookings ORDER BY id";

        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                bookings.add(mapRow(resultSet));
            }

            return bookings;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Booking findById(Integer id) {

        String sql = "SELECT id, user_id, room_id, start_time, end_time, status FROM bookings WHERE id = ?";

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

    public List<Booking> findByUserId(int userId) {

        String sql = "SELECT id, user_id, room_id, start_time, end_time, status FROM bookings WHERE user_id = ? ORDER BY id";

        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapRow(resultSet));
                }
            }

            return bookings;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Booking> findByStatus(BookingStatus status) {

        String sql = "SELECT id, user_id, room_id, start_time, end_time, status FROM bookings WHERE status = ? ORDER BY id";

        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.getDbValue());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapRow(resultSet));
                }
            }

            return bookings;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsOverlapping(int roomId, LocalDateTime startTime, LocalDateTime endTime) {

        String sql = """
                SELECT 1 FROM bookings
                WHERE room_id = ?
                  AND status = ?
                  AND start_time < ?
                  AND end_time > ?
                LIMIT 1
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, roomId);
            statement.setString(2, BookingStatus.ACTIVE.getDbValue());
            statement.setTimestamp(3, Timestamp.valueOf(endTime));
            statement.setTimestamp(4, Timestamp.valueOf(startTime));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateStatus(int id, BookingStatus status) {

        String sql = "UPDATE bookings SET status = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.getDbValue());
            statement.setInt(2, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {

        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Booking mapRow(ResultSet resultSet) throws SQLException {
        return new Booking(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("room_id"),
                resultSet.getTimestamp("start_time").toLocalDateTime(),
                resultSet.getTimestamp("end_time").toLocalDateTime(),
                BookingStatus.fromDbValue(resultSet.getString("status"))
        );
    }
}
