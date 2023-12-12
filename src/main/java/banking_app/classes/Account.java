package banking_app.classes;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
    private final long accountId;
    private String name;
    private float transactionLimit;
    private final Date dateCreated;
    private final int userId;

    private final float balance;

    public Account(long accountId, String name, float transactionLimit,
                   Date dateCreated, int userId, float balance) {
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
                resultSet.getFloat("balance"));
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

    public float getBalance() {
        return balance;
    }

    public void showBalance() {
        System.out.println("==============================");
        System.out.println("Stan konta: " + String.format("%.2f", this.getBalance()) + " zł");
        System.out.println("==============================");

    }
}
