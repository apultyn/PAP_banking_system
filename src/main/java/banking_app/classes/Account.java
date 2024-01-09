package banking_app.classes;

import banking_exceptions.InvalidAmountException;
import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static banking_app.classes.User.isBigDecimal;

public class Account {
    private final long accountId;
    private String name;
    private float transactionLimit;
    private final Date dateCreated;
    private final int userId;

    private final BigDecimal balance;

    public Account(long accountId, String name, float transactionLimit,
                   Date dateCreated, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.name = name;
        this.transactionLimit = transactionLimit;
        this.dateCreated = dateCreated;
        this.userId = userId;
        this.balance = balance;
    }

    public Account(ResultSet resultSet) throws SQLException {

        this(resultSet.getLong("account_id"),
                resultSet.getString("name"),
                resultSet.getFloat("transaction_limit"),
                resultSet.getDate("creation_date"),
                resultSet.getInt("owner_id"),
                resultSet.getBigDecimal("balance"));
    }

    public void updateTransactionLimit(ConnectionManager manager, String transferLimit) throws SQLException, InvalidAmountException{
        if (transferLimit.isEmpty())
            throw new InvalidAmountException("Transfer limit cannot be empty!");
        if (!isBigDecimal(transferLimit))
            throw new InvalidAmountException("Transfer limit must be a number!");
        if (new BigDecimal(transferLimit).compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Transfer limit must be positive!");
        manager.updateAccountsLimit(accountId, new BigDecimal(transferLimit));
    }

    public long getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public float getTransactionLimit() {
        return transactionLimit;
    }

    public Date getDateCreated() {
        return dateCreated;
    }


    public int getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setTransactionLimit(float newLimit) {
        this.transactionLimit = newLimit;
    }

    public void showBalance() {
        System.out.println("==============================");
        System.out.println("Stan konta: " + String.format("%.2f", this.getBalance()) + " zł");
        System.out.println("==============================");
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", name='" + name + '\'' +
                ", transactionLimit=" + transactionLimit +
                ", dateCreated=" + dateCreated +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}
