package connections;


import banking_app.classes.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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
    public List<Transaction> findTransactionsByReciever(int reciever_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions" +
                "WHERE ACCOUNTS_ACCOUNT_ID2 = " + reciever_id;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()) {
                    int transactionId = resultSet.getInt("transaction_id");

                    int sourceId = resultSet.getInt("senger_id");
                    int targetId = resultSet.getInt("reciever_id");
                    float amount = resultSet.getFloat("amount");
                    Date date = resultSet.getDate("date");
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

    public List<Transaction> findTransactionsBySender(int sender_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions "+
                "WHERE accounts_account_id = " + sender_id;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()){
                    int transactionId = resultSet.getInt("transaction_id");
                    Date transactionDate =  resultSet.getDate("date");
                    int sourceId = resultSet.getInt("senger_id");
                    int targetId = resultSet.getInt("reciever_id");
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
