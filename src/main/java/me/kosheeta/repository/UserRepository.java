package me.kosheeta.repository;

import me.kosheeta.model.User;
import me.kosheeta.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    public User save(User user) {

        String sql = """
                INSERT INTO users (name, email)
                VALUES (?, ?)
                RETURNING id
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
