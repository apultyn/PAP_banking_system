package connections;


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
    public void registerUser() {

    }
}
