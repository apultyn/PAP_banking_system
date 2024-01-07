package banking_app.classes;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Deposit {
    private final int depositId;
    private String name;
    private BigDecimal amount;
    private BigDecimal rate;
    private long ownerAccId;
    private Date start, end;

    public Deposit(int depositId, String name, BigDecimal amount,
                   BigDecimal rate, long ownerId, Date start, Date end) {
        this.depositId = depositId;
        this.rate = rate;
        this.name = name;
        this.amount = amount;
        this.ownerAccId = ownerId;
        this.start = start;
        this.end = end;
    }

    public Deposit(ResultSet resultSet) throws SQLException {

        this(resultSet.getInt("deposit_Id"),
                resultSet.getString("name"),
                resultSet.getBigDecimal("amount"),
                resultSet.getBigDecimal("rate"),
                resultSet.getLong("owner_acc_id"),
                resultSet.getDate("start_date"),
                resultSet.getDate("end_date"));
    }
    public int getDepositId() {
        return depositId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public long getOwnerId() {
        return ownerAccId;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return  end;
    }
}
