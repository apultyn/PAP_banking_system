package connections;


import banking_app.classes.Account;
import banking_app.classes.Transaction;
import banking_app.classes.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ConnectionManager {
    public Connection connection;

    public ConnectionManager() throws SQLException {
        connection = OracleConnectionManager.getConnection();
    }

    public void Close() throws SQLException{
        connection.close();
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

    public void registerUser(String name, String surname, String email, String password) throws SQLException {
        String sqlInsert = "INSERT INTO users (name, surname, email, password) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, surname);
        preparedStatement.setString(3, email);
        preparedStatement.setString(4, password);
        preparedStatement.executeUpdate();
    }

    public List<Transaction> findTransactionsByReciever(int reciever_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions" +
                "WHERE reciver_id = " + reciever_id;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()) {
                    int transactionId = resultSet.getInt("transaction_id");
                    int sourceId = resultSet.getInt("sender_id");
                    int targetId = resultSet.getInt("reciver_id");
                    float amount = resultSet.getFloat("amount");
                    Date date = resultSet.getDate("date_made");
                    int type = resultSet.getInt("type");
                    String title = resultSet.getString("title");
                    Transaction newTransaction = new Transaction(transactionId, sourceId,
                            targetId, date, amount, type, title);
                    outcome.add(newTransaction);
                }
                return outcome;
            }
        }
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
    public Account findAccount(int accountId) throws SQLException {
        String sqlQuerry = "SELECT * FROM accounts WHERE account_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuerry);
        preparedStatement.setInt(1, accountId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new Account(resultSet);
        }
        return null;
    }
    public void registerTransaction(Transaction newTransaction) throws SQLException{
        String sqlInsert = "INSERT INTO transactions (transaction_id, title, amount, date_made, type, sender_id, reciver_id) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setNull(1, java.sql.Types.INTEGER);
        preparedStatement.setString(2, newTransaction.getTitle());
        preparedStatement.setFloat(3, newTransaction.getAmount());
        preparedStatement.setDate(4, newTransaction.getDate());
        preparedStatement.setInt(5, newTransaction.getType());
        preparedStatement.setInt(6, newTransaction.getSourceId());
        preparedStatement.setInt(7, newTransaction.getTargetId());
        preparedStatement.executeUpdate();
    }

    public List<Transaction> findTransactionsBySender(int sender_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions "+
                "WHERE sender_id = " + sender_id;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()){
                    int transactionId = resultSet.getInt("transaction_id");
                    Date transactionDate =  resultSet.getDate("date_made");
                    int sourceId = resultSet.getInt("sender_id");
                    int targetId = resultSet.getInt("reciver_id");
                    Date date = resultSet.getDate("date");
                    float amount = resultSet.getFloat("amount");
                    int type = resultSet.getInt("type");
                    String title = resultSet.getString("title");
                    Transaction newTransaction = new Transaction(transactionId, sourceId,
                            targetId, date, amount, type, title);
                    outcome.add(newTransaction);
                }
                return outcome;
            }
        }
    }

    public List<Transaction> findTransactionsByUser(int user_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions "+
                "WHERE sender_id= " + user_id + " OR reciver_id=" + user_id +
                " ORDER BY date DESC";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()){
                    int transactionId = resultSet.getInt("transaction_id");
                    Date transactionDate =  resultSet.getDate("date_made");
                    int sourceId = resultSet.getInt("sender_id");
                    int targetId = resultSet.getInt("reciver_id");
                    Date date = resultSet.getDate("date");
                    float amount = resultSet.getFloat("amount");
                    int type = resultSet.getInt("type");
                    String title = resultSet.getString("title");
                    Transaction newTransaction = new Transaction(transactionId, sourceId,
                            targetId, date, amount, type, title);
                    outcome.add(newTransaction);
                }
                return outcome;
            }
        }
    }
}
