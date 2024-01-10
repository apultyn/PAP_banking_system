package banking_app.classes;

import banking_exceptions.InvalidAmountException;
import banking_exceptions.InvalidNameException;
import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static banking_app.classes.User.isBigDecimal;

public class Account {
    private final Long accountId;
    private final String name;
    private BigDecimal transferLimit;
    private final Date dateCreated;
    private Integer userId;
    private final BigDecimal balance;

    public Account(long accountId, String name, BigDecimal transferLimit,
                   Date dateCreated, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.name = name;
        this.transferLimit = transferLimit;
        this.dateCreated = dateCreated;
        this.userId = userId;
        this.balance = balance;
    }

    public Account(int userId, String name, BigDecimal transferLimit) {
        this.userId = userId;
        this.name = name;
        this.transferLimit = transferLimit;
        this.accountId = null;
        this.dateCreated = null;
        this.balance = BigDecimal.ZERO;
    }

    public Account(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("account_id"),
                resultSet.getString("name"),
                resultSet.getBigDecimal("transfer_limit"),
                resultSet.getDate("creation_date"),
                resultSet.getInt("owner_id"),
                resultSet.getBigDecimal("balance"));
    }

    public Account(String name, String transferLimit, Integer userId) throws InvalidNameException, InvalidAmountException {
        if (name.isEmpty())
            throw new InvalidNameException("Name cannot be empty!");
        if (transferLimit.isEmpty())
            throw new InvalidAmountException("Transfer limit cannot be empty!");
        if (!isBigDecimal(transferLimit))
            throw new InvalidAmountException("Transfer limit must be a number!");
        if (new BigDecimal(transferLimit).compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Transfer limit must be positive!");
        this.accountId = null;
        this.name = name;
        this.transferLimit = new BigDecimal(transferLimit);
        this.dateCreated = null;
        this.userId = userId;
        this.balance = BigDecimal.ZERO;
    }

    public void updateTransferLimit(ConnectionManager manager, String transferLimit) throws SQLException, InvalidAmountException{
        if (transferLimit.isEmpty())
            throw new InvalidAmountException("Transfer limit cannot be empty!");
        if (!isBigDecimal(transferLimit))
            throw new InvalidAmountException("Transfer limit must be a number!");
        if (new BigDecimal(transferLimit).compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Transfer limit must be positive!");
        manager.updateTransferLimit(accountId, new BigDecimal(transferLimit));
    }

    public long getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTransferLimit() {
        return transferLimit;
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

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTransferLimit(BigDecimal newLimit) {
        this.transferLimit = newLimit;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", name='" + name + '\'' +
                ", transferLimit=" + transferLimit +
                ", dateCreated=" + dateCreated +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}
