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

    public void registerUser(User user) throws SQLException {
        String sqlInsert = "INSERT INTO users (name, surname, email, password, pin) values (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getSurname());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getPassword());
        preparedStatement.setString(5, user.getPin());
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
        String sqlInsert = "INSERT INTO accounts (name, transfer_limit, owner_id) values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, account.getName());
        preparedStatement.setBigDecimal(2, account.getTransferLimit());
        preparedStatement.setInt(3, account.getUserId());
        preparedStatement.executeUpdate();
    }

    public User findUserFromAccount(long accountId) throws SQLException {
        String sqlQuery = "SELECT * FROM users WHERE user_id = (" +
                "SELECT owner_id FROM accounts WHERE account_id = ?) ";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setLong(1, accountId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new User(resultSet);
        }
        return null;
    }

    public List<Account> findUsersAccounts(int user_id) throws SQLException {
        String sqlQuery = "SELECT * FROM accounts WHERE owner_id = ? ORDER BY account_id ASC";
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

    public void addBalance(long accountId, BigDecimal amount) throws SQLException {
        String sqlQuery = "UPDATE ACCOUNTS SET BALANCE = BALANCE + ? WHERE ACCOUNT_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setBigDecimal(1, amount);
        preparedStatement.setLong(2, accountId);
        preparedStatement.executeUpdate();
    }

    public void registerTransfer(Transfer transfer) throws SQLException {
        String sqlInsert = "INSERT INTO transfers (title, amount, type, sender_id, receiver_id) values (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, transfer.getTitle());
        preparedStatement.setBigDecimal(2, transfer.getAmount());
        preparedStatement.setInt(3, transfer.getType());
        preparedStatement.setLong(4, transfer.getSourceId());
        preparedStatement.setLong(5, transfer.getTargetId());
        preparedStatement.executeUpdate();
    }

    public List<Transfer> findTransfersBySender(long sender_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transfers WHERE sender_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, sender_id);
            return getTransfers(preparedStatement);
        }
    }

    public List<Transfer> findTransfersByReceiver(long receiver_id) throws SQLException {
        String sqlQuery = "SELECT * FROM transfers WHERE receiver_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, receiver_id);
            return getTransfers(preparedStatement);
        }
    }

    private List<Transfer> getTransfers(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Transfer> outcome = new ArrayList<>();
            while (resultSet.next()) {
                Transfer newTransfer = new Transfer(resultSet);
                outcome.add(newTransfer);
            }
            return outcome;
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


    public void createAutomaticSaving(AutomaticSaving as) throws SQLException {
        String sqlInsert = "INSERT INTO automatic_savings (name, sender_id, receiver_id, amount) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, as.getName());
        preparedStatement.setLong(2, as.getSourceAccountId());
        preparedStatement.setLong(3, as.getTargetAccountId());
        preparedStatement.setBigDecimal(4, as.getAmount());
        preparedStatement.executeUpdate();
    }

    public void createStandingOrder(StandingOrder standingOrder) throws SQLException {
        String sqlInsert = "INSERT INTO standing_orders (name, amount, sender_id, receiver_id) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, standingOrder.getName());
        preparedStatement.setBigDecimal(2, standingOrder.getAmount());
        preparedStatement.setLong(3, standingOrder.getSourceAccountId());
        preparedStatement.setLong(4, standingOrder.getTargetAccountId());
        preparedStatement.executeUpdate();
    }

    public void createContact(String name, long account_id, int owner_id) throws SQLException {
        String sqlInsert = "INSERT INTO contacts (name, accounts_account_id, owner_id) values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setString(1, name);
        preparedStatement.setLong(2, account_id);
        preparedStatement.setInt(3, owner_id);
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

    public void updateUserPin(int user_id, String pin) throws SQLException {
        System.out.println("Entered connection function");
        String sqlUpdate = "UPDATE users SET pin = ? WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
        preparedStatement.setString(1, pin);
        preparedStatement.setInt(2, user_id);
        preparedStatement.executeUpdate();
    }


    public void updateTransferLimit(long account_id, BigDecimal newLimit) throws SQLException {
        String sqlQuery = "UPDATE accounts SET transfer_limit= ? WHERE account_id= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setBigDecimal(1, newLimit);
            preparedStatement.setLong(2, account_id);
            preparedStatement.executeUpdate();
        }
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
            List<AutomaticSaving> savingsSingleAcc;
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
    }
    public void createLoan(BigDecimal amount, BigDecimal rate, Date end,long ownerAccId, BigDecimal fixed) throws
            SQLException {
        String sqlInsert = "INSERT INTO loans (amount, rate, finish_date, owner_acc_id, fixed_rate) values (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setBigDecimal(1, amount);
        preparedStatement.setBigDecimal(2, rate);
        preparedStatement.setDate(3, end);
        preparedStatement.setLong(4, ownerAccId);
        preparedStatement.setBigDecimal(5, fixed);
        preparedStatement.executeUpdate();
    }

    public List<Loan> findLoansByAccId ( long accId) throws SQLException {
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

    public List<Loan> findUsersLoans ( int user_id) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        accounts = findUsersAccounts(user_id);

        List<Loan> loans = new ArrayList<>();
        for (Account a : accounts) {
            List<Loan> loansSingleAcc;
            loansSingleAcc = findLoansByAccId(a.getAccountId());
            loans.addAll(loansSingleAcc);
        }
        return loans;
    }
}

