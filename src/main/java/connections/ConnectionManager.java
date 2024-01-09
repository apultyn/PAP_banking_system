package connections;


import banking_app.classes.*;

import java.math.BigDecimal;
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

    public Account findAccount(int userId, String name) throws SQLException {
        String sqlQuerry = "SELECT * FROM accounts WHERE owner_id = ? AND name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuerry);
        preparedStatement.setLong(1, userId);
        preparedStatement.setString(2, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new Account(resultSet);
        }
        return null;
    }

    public void createAccount(Account account) throws SQLException {
        String sqlInsert = "INSERT INTO accounts (name, transaction_limit, owner_id) values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, account.getName());
        preparedStatement.setBigDecimal(2, account.getTransactionLimit());
        preparedStatement.setInt(3, account.getUserId());
        preparedStatement.executeUpdate();
    }

    public void createAccount(String name, BigDecimal transactionLimit, int ownerId) throws SQLException {
        String sqlInsert = "INSERT INTO accounts (name, transaction_limit, owner_id) values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, name);
        preparedStatement.setBigDecimal(2, transactionLimit);
        preparedStatement.setInt(3, ownerId);
        preparedStatement.executeUpdate();
    }

    public void createAccount(String name, int ownerId) throws SQLException {
        String sqlInsert = "INSERT INTO accounts (name, owner_id) values (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, ownerId);
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

    public void addBalance(long accountId, BigDecimal amount) throws SQLException{
        String sqlQuery = "UPDATE ACCOUNTS SET BALANCE = BALANCE + ? WHERE ACCOUNT_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setBigDecimal(1, amount);
        preparedStatement.setLong(2, accountId);
        preparedStatement.executeUpdate();
    }

    public void registerTransaction(Transaction transaction) throws SQLException{
        String sqlInsert = "INSERT INTO transactions (title, amount, type, sender_id, reciver_id) values (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, transaction.getTitle());
        preparedStatement.setBigDecimal(2, transaction.getAmount());
        preparedStatement.setInt(3, transaction.getType());
        preparedStatement.setLong(4, transaction.getSourceId());
        preparedStatement.setLong(5, transaction.getTargetId());
        preparedStatement.executeUpdate();
    }

    public List<Transaction> findTransactionsBySender(long sender_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions WHERE sender_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setLong(1, sender_id);
            return getTransactions(preparedStatement);
        }
    }

    public List<Transaction> findTransactionsByReceiver(long receiver_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions WHERE reciver_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, receiver_id);
            return getTransactions(preparedStatement);
        }
    }

    private List<Transaction> getTransactions(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Transaction> outcome = new ArrayList<>();
            while (resultSet.next()) {
                Transaction newTransaction = new Transaction(resultSet);
                outcome.add(newTransaction);
            }
            return outcome;
        }
    }

    public List<Transaction> findTransactionsByAccount(long account_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transactions WHERE sender_id= ? OR reciver_id= ? ORDER BY DATE_MADE DESC";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setLong(1, account_id);
            preparedStatement.setLong(2, account_id);
            return getTransactions(preparedStatement);
        }
    }

    public void setTransactionLimit(long account_id, float newLimit) throws SQLException{
        String sqlQuery = "UPDATE accounts SET transaction_limit= ? WHERE account_id= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setFloat(1, newLimit);
            preparedStatement.setLong(2, account_id);
            preparedStatement.executeUpdate();
        }
    }

    public void createDeposit(Deposit deposit) throws SQLException {
        String sqlInsert = "INSERT INTO deposits (name, rate, end_date, amount, owner_acc_id) values (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, deposit.getName());
        preparedStatement.setBigDecimal(2, deposit.getRate());
        preparedStatement.setDate(3, deposit.getEnd());
        preparedStatement.setBigDecimal(4, deposit.getAmount());
        preparedStatement.setLong(5, deposit.getOwnerAccId());
        preparedStatement.executeUpdate();
    }

    public Boolean checkDepositName(String name, long ownerId) throws SQLException {
        String sqlQuery = "SELECT * FROM deposits WHERE owner_acc_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setLong(1, ownerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Deposit deposit = new Deposit(resultSet);
            if (deposit.getName().equals(name))
                return false;
        }
        return true;
    }

    public List<Deposit> findUsersDeposits(int user_id) throws SQLException {
        List<Account> listAccounts = findUsersAccounts(user_id);
        List<Long> accountIds = new ArrayList<>();
        for (Account account : listAccounts) {
            accountIds.add(account.getAccountId());
        }

        if (accountIds.isEmpty()) {
            return new ArrayList<>(); // Return empty list if no accounts found
        }

        String sql = createSqlQuery(accountIds);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < accountIds.size(); i++) {
                pstmt.setLong(i + 1, accountIds.get(i)); // Set account ID for each placeholder
            }
            ResultSet rs = pstmt.executeQuery();
            List<Deposit> deposits = new ArrayList<>();
            while (rs.next()) {
                deposits.add(new Deposit(rs));
            }
            return deposits;
        }
    }

    private String createSqlQuery(List<Long> accountIds) {
        StringBuilder builder = new StringBuilder("SELECT * FROM deposits WHERE owner_acc_id IN (");
        for (int i = 0; i < accountIds.size(); i++) {
            builder.append("?");
            if (i < accountIds.size() - 1) {
                builder.append(",");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    public Boolean checkAmountAtAccount(BigDecimal amount, long AccountId) throws SQLException {
        String sqlQuery = "SELECT * FROM accounts WHERE account_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setLong(1, AccountId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Account account = new Account(resultSet);
            return account.getBalance().compareTo(amount) > 0;
        }
        return false;
    }

    public void createAutomaticSaving(String name, long sender_id, long reciever_id, BigDecimal amount) throws SQLException {
        String sqlInsert = "INSERT INTO automatic_savings (name, sender_id, reciever_id, amount) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, name);
        preparedStatement.setLong(2, sender_id);
        preparedStatement.setLong(3, reciever_id);
        preparedStatement.setBigDecimal(4, amount);
        preparedStatement.executeUpdate();
    }

    public void deleteAutomaticSaving(int saving_id) throws SQLException {
        String sqlInsert = "DELETE FROM automatic_savings WHERE saving_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setLong(1, saving_id);
        preparedStatement.executeUpdate();
    }

    public void createStadningOrder(String name, BigDecimal amount, long sender_id, long reciever_id ) throws SQLException {
        String sqlInsert = "INSERT INTO standing_orders (name, amount, sender_id, reciever_id) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, name);
        preparedStatement.setBigDecimal(2, amount);
        preparedStatement.setLong(3, sender_id);
        preparedStatement.setLong(4, reciever_id);
        preparedStatement.executeUpdate();
    }

    public void deleteStadingOrder(long orderId) throws SQLException {
        String sqlInsert = "DELETE FROM standing_orders WHERE order_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setLong(1, orderId);
        preparedStatement.executeUpdate();
    }

    public void createContact(String name, long account_id) throws SQLException {
        String sqlInsert = "INSERT INTO contacts (name, accounts_account_id) values (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, name);
        preparedStatement.setLong(2, account_id);
        preparedStatement.executeUpdate();
    }

    public void deleteContact(int contact_id) throws SQLException {
        String sqlDelete = "DELETE FROM contacts WHERE contact_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete);
        preparedStatement.setInt(1, contact_id);
        preparedStatement.executeUpdate();
    }

    public void updateUserFirstName(int user_id, String name) throws SQLException {
        String sqlUpdate = "UPDATE users SET name = ? WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, user_id);
        preparedStatement.executeUpdate();
    }

    public void updateUserSurname(int user_id, String surname) throws SQLException {
        String sqlUpdate = "UPDATE users SET surname = ? WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
        preparedStatement.setString(1, surname);
        preparedStatement.setInt(2, user_id);
        preparedStatement.executeUpdate();
    }

    public void updateUserEmail(int user_id, String email) throws SQLException {
        String sqlUpdate = "UPDATE users SET email = ? WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
        preparedStatement.setString(1, email);
        preparedStatement.setInt(2, user_id);
        preparedStatement.executeUpdate();
    }

    public void updateUserPassword(int user_id, String password) throws SQLException {
        String sqlUpdate = "UPDATE users SET password = ? WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
        preparedStatement.setString(1, password);
        preparedStatement.setInt(2, user_id);
        preparedStatement.executeUpdate();
    }

    public void updateAccountsLimit(long account_id, BigDecimal amount) throws SQLException{
        String sqlUpdate = "UPDATE accounts SET transaction_limit = ? WHERE account_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
        preparedStatement.setBigDecimal(1, amount);
        preparedStatement.setLong(2, account_id);
        preparedStatement.executeUpdate();
    }

    public List<AutomaticSaving> findSavingsBySenderAcc(long accId) throws SQLException {
        String sqlQuery = "SELECT * FROM automatic_savings WHERE sender_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setLong(1, accId);

        List<AutomaticSaving> savings = new ArrayList<>();
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                AutomaticSaving saving = new AutomaticSaving(resultSet);
                savings.add(saving);
            }
        }
        return savings;
    }

    public List<AutomaticSaving> findUsersSavings(int user_id) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        accounts = findUsersAccounts(user_id);

        List<AutomaticSaving> savings = new ArrayList<>();
        for (Account a : accounts) {
            List<AutomaticSaving> savingsSingleAcc = new ArrayList<>();
            savingsSingleAcc = findSavingsBySenderAcc(a.getAccountId());
            savings.addAll(savingsSingleAcc);
        }
        return savings;
    }

    public List<StandingOrder> findOrdersBySenderAcc(long accId) throws SQLException {
        String sqlQuery = "SELECT * FROM standing_orders WHERE sender_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setLong(1, accId);

        List<StandingOrder> orders = new ArrayList<>();
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                StandingOrder order = new StandingOrder(resultSet);
                orders.add(order);
            }
        }
        return orders;
    }

    public List<StandingOrder> findUsersOrders(int user_id) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        accounts = findUsersAccounts(user_id);

        List<StandingOrder> orders = new ArrayList<>();
        for (Account a : accounts) {
            List<StandingOrder> ordersSingleAcc = new ArrayList<>();
            ordersSingleAcc = findOrdersBySenderAcc(a.getAccountId());
            orders.addAll(ordersSingleAcc);
        }
        return orders;
    }


    public List<Contact> findUsersContacts(int user_id) throws SQLException {
        String sqlQuery = "SELECT * FROM contacts WHERE owner_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setInt(1, user_id);

        List<Contact> contacts = new ArrayList<>();
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Contact contact = new Contact(resultSet);
                contacts.add(contact);
            }
        }
        return contacts;
    public void createLoan(BigDecimal amount, BigDecimal rate, Date end, long owneraccId, BigDecimal fixed) throws SQLException {
        String sqlInsert = "INSERT INTO loans (amount, rate, finish_date, owner_acc_id, fixed_rate) values (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setBigDecimal(1, amount);
        preparedStatement.setBigDecimal(2, rate);
        preparedStatement.setDate(3, end);
        preparedStatement.setLong(4, owneraccId);
        preparedStatement.setBigDecimal(5, fixed);
        preparedStatement.executeUpdate();
    }

    public List<Loan> findLoansByAccId(long accId) throws SQLException {
        String sqlQuery = "SELECT * FROM loans WHERE OWNER_ACC_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setLong(1, accId);

        List<Loan> loans = new ArrayList<>();
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Loan loan = new Loan(resultSet);
                loans.add(loan);
            }
        }
        return loans;
    }

    public List<Loan> findUsersLoans(int user_id) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        accounts = findUsersAccounts(user_id);

        List<Loan> loans = new ArrayList<>();
        for (Account a : accounts) {
            List<Loan> loansSingleAcc = new ArrayList<>();
            loansSingleAcc = findLoansByAccId(a.getAccountId());
            loans.addAll(loansSingleAcc);
        }
        return loans;
    }
}
