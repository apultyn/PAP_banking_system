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
    public List<Transaction> findTransactionsByReciever(int reciever_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions" +
                "WHERE reciver_id = " + reciever_id + ";";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()) {
                    int transactionId = resultSet.getInt("transaction_id");

                    int sourceId = resultSet.getInt("sender_id");
                    int targetId = resultSet.getInt("reciver_id");
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

    public void registerTransaction(Transaction newTransaction) throws SQLException{
        String sqlInsert = "INSERT INTO transactions (title, amount, date, " +
                "type, sender_id, reciver_id) values (?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        //preparedStatement.setInt(1, newTransaction.getTransactionId());
        preparedStatement.setString(1, newTransaction.getTitle());
        preparedStatement.setFloat(2, newTransaction.getAmount());
        preparedStatement.setDate(3, newTransaction.getDate());
        preparedStatement.setInt(4, newTransaction.getType());
        preparedStatement.setInt(5, newTransaction.getSourceId());
        preparedStatement.setInt(6, newTransaction.getTargetId());
        preparedStatement.executeUpdate();
    }

    public List<Transaction> findTransactionsBySender(int sender_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions "+
                "WHERE sender_id = " + sender_id + ";";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()){
                    int transactionId = resultSet.getInt("transaction_id");
                    Date transactionDate =  resultSet.getDate("date");
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
                " ORDER BY date DESC;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()){
                    int transactionId = resultSet.getInt("transaction_id");
                    Date transactionDate =  resultSet.getDate("date");
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
