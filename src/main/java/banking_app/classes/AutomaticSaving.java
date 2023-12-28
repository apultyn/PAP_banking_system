package banking_app.classes;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutomaticSaving {
    private final int savingID;
    private String name;
    private Date dateStarted;
    private long sourceAccountId;
    private long targetAccountId;

    private BigDecimal amount;

    public AutomaticSaving(int savingID, String name, Date started, long sender_id, long reciever_id, BigDecimal am) {
        this.savingID = savingID;
        this.name = name;
        this.dateStarted = started;
        this.sourceAccountId = sender_id;
        this.targetAccountId = reciever_id;
        this.amount = am;
    }

    public AutomaticSaving(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt("saving_id"),
                resultSet.getString("name"),
                resultSet.getDate("start_date"),
                resultSet.getLong("sender_id"),
                resultSet.getLong("reciever_id"),
                resultSet.getBigDecimal("amount"));
    }

    public int getSavingID() {
        return savingID;
    }

    public String getName() {
        return name;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public long getSourceAccountId() {
        return sourceAccountId;
    }

    public long getTargetAccountId() {
        return targetAccountId;
    }

    public  BigDecimal getAmount() { return amount; }
}
