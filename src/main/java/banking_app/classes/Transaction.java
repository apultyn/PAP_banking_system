package banking_app.classes;

import banking_exceptions.InvalidAccountNumberException;
import banking_exceptions.InvalidAmountException;
import banking_exceptions.InvalidNameException;
import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static banking_app.classes.User.isBigDecimal;

public class Transaction
{
    private int transactionId;
    private long sourceId, targetId;
    private BigDecimal amount;
    private int type;
    private String title;

    private Date date;

    public Transaction(long targetId, long sourceId, String title, BigDecimal amount, int type) {

        this.targetId = targetId;
        this.sourceId = sourceId;
        this.title = title;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(int transactionId, long sourceId, long targetId,
                       Date date, BigDecimal amount, int type, String title){
        this.transactionId = transactionId;
        this.date = date;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.amount = amount;
        this.type = type;
        this.title = title;
    }
    public Transaction(String recipientName, String recipientAccountNumber,
                       String senderAccountNumber, String title, String amount) throws InvalidNameException, InvalidAccountNumberException, InvalidAmountException {
        if (recipientName.isEmpty())
            throw new InvalidNameException("Recipient name cannot be empty!");
        if (!recipientAccountNumber.matches("\\d{16}"))
            throw new InvalidAccountNumberException("Number must be 16 digits long!");
        if (title.isEmpty())
            throw new InvalidNameException("Title cannot be empty!");
        if (amount.isEmpty())
            throw new InvalidAmountException("Amount cannot be empty!");
        if (!isBigDecimal(amount))
            throw new InvalidAmountException("Amount must be a number!");
        if (new BigDecimal(amount).compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Amount must be positive!");
        this.targetId = Long.parseLong(recipientAccountNumber);
        this.sourceId = Long.parseLong(senderAccountNumber);
        this.title = title;
        this.amount = new BigDecimal(amount);
        this.type = 1;
    }


    public Transaction(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt("transaction_id"),
                resultSet.getLong("sender_id"),
                resultSet.getLong("reciver_id"),
                resultSet.getDate("date_made"),
                resultSet.getBigDecimal("amount"),
                resultSet.getInt("type"),
                resultSet.getString("title"));
    }

    public int getTransactionId(){
        return transactionId;
    }
    public long getSourceId() {
        return sourceId;
    }
    public long getTargetId() {
        return targetId;
    }
    public Date getDate() {
        return date;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public String getTitle() {
        return title;
        //abc
    }
    public int getType() {
        return type;
    }


    public static boolean isValidAccountId(ConnectionManager connectionManager, long accountId) throws SQLException {
        return connectionManager.findAccount(accountId) != null;
    }

}
