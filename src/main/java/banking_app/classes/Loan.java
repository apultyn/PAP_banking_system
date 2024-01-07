package banking_app.classes;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Loan {
    private final int loanId;
    private BigDecimal amount;
    private BigDecimal rate;
    private int ownerAccId;
    private Date start, end;

    public Loan(int loanId, BigDecimal amount, Date start,
                   BigDecimal rate, Date end, int ownerId) {
        this.loanId = loanId;
        this.rate = rate;
        this.amount = amount;
        this.ownerAccId = ownerId;
        this.start = start;
        this.end = end;
    }

    public Loan(ResultSet resultSet) throws SQLException {

        this(resultSet.getInt("depositId"),
                resultSet.getBigDecimal("amount"),
                resultSet.getDate("start_date"),
                resultSet.getBigDecimal("rate"),
                resultSet.getDate("finish_date"),
                resultSet.getInt("owner_acc_id"));
    }
}
