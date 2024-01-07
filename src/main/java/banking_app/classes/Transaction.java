package banking_app.classes;

import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction
{
    private int transactionId;
    private long sourceId, targetId;
    private BigDecimal amount;
    private int type;
    private String title;

    private Date date;

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
    public Transaction(long sourceId, long targetId, Date date, BigDecimal amount, int type, String title)
    {
        this.date = date;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.amount = amount;
        this.type = type;
        this.title = title;
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
