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

    public List<Transaction> findTransactionsByReciever(long reciever_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions" +
                "WHERE reciver_id = " + reciever_id;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()) {
                    Transaction newTransaction = new Transaction(resultSet);
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

    public Account findAccount(long accountId) throws SQLException {
        String sqlQuerry = "SELECT * FROM accounts WHERE account_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuerry);
        preparedStatement.setLong(1, accountId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new Account(resultSet);
        }
        return null;
    }

    public void createAccount(String name, Float transactionLimit, int ownerId) throws SQLException {
        String sqlInsert = "INSERT INTO accounts (name, transaction_limit, owner_id) values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, name);
        preparedStatement.setFloat(2, transactionLimit);
        preparedStatement.setInt(3, ownerId);

        preparedStatement.executeUpdate();

    }


    public List<Account> findUsersAccounts(int user_id) throws SQLException {
        String sqlQuery = "SELECT * FROM accounts WHERE owner_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setInt(1, user_id);
        List<Account> accounts = new ArrayList<>();

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Account account = new Account(resultSet);
                accounts.add(account);
            }
        }

        return accounts;
    }

    public void registerTransaction(String title, float amount, int type, long sourceId, long targetId) throws SQLException{
        String sqlInsert = "INSERT INTO transactions (title, amount, type, sender_id, reciver_id) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(2, title);
        preparedStatement.setFloat(3, amount);
        preparedStatement.setInt(4, type);
        preparedStatement.setLong(5, sourceId);
        preparedStatement.setLong(6, targetId);
        preparedStatement.executeUpdate();
    }

    public List<Transaction> findTransactionsBySender(int sender_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions "+
                "WHERE sender_id = " + sender_id;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()) {
                    Transaction newTransaction = new Transaction(resultSet);
                    outcome.add(newTransaction);
                }
                return outcome;
            }
        }
    }

    public List<Transaction> findTransactionsByAccount(int account_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions "+
                "WHERE sender_id= " + account_id + " OR reciver_id=" + account_id +
                " ORDER BY DATE_MADE DESC";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Transaction> outcome = new ArrayList<Transaction>();
                while (resultSet.next()){
                    Transaction newTransaction = new Transaction(resultSet);
                    outcome.add(newTransaction);
                }
                return outcome;
            }
        }
    }

//    public List<Transaction> findTransactionsByUser(int id) {
//    }
}
