package connections;


import banking_app.classes.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ConnectionManager {
    public Connection connection;

    public ConnectionManager() throws SQLException {
        connection = OracleConnectionManager.getConnection();
    }
    public String example() throws SQLException {

        String sqlQuery = "SELECT * FROM users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    String name = resultSet.getString("name");

                    System.out.println("User ID: " + userId + ", Name: " + name);
                }
            }
        }
        return "Worked";
    }
    public void registerUser(User newUser) throws SQLException {

        String sqlInsert = "INSERT INTO users (name, surname, email, password) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, newUser.getName());
        preparedStatement.setString(2, newUser.getSurname());
        preparedStatement.setString(3, newUser.getEmail());
        preparedStatement.setString(4, newUser.getPassword());
        preparedStatement.executeUpdate();


    }

    public User findUser(String email) throws SQLException {
        String sqlQuerry = "SELECT * FROM users WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuerry);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new User(resultSet);
        }
        return null;
    }

}
