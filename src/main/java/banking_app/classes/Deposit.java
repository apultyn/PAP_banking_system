package banking_app.classes;

import banking_exceptions.AccountNotFoundException;
import banking_exceptions.DepositNameExistingException;
import banking_exceptions.NotEnoughFundsException;
import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;

public class Deposit {
    private final int depositId;
    private final String name;
    private final BigDecimal amount;
    private final BigDecimal rate;
    private final long ownerAccId;
    private final Date start;
    private final Date end;

    public Deposit(int depositId, String name, BigDecimal amount,
                   BigDecimal rate, long ownerAccId, Date start, Date end) {
        this.depositId = depositId;
        this.rate = rate;
        this.name = name;
        this.amount = amount;
        this.ownerAccId = ownerAccId;
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

    public long getOwnerAccId() {
        return ownerAccId;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
    public void createDeposit(ConnectionManager manager) throws DepositNameExistingException, SQLException, NotEnoughFundsException, AccountNotFoundException {
        if (start.after(end))
            throw new DateTimeException("End date can't be earlier than today!");
        if (manager.findAccount(ownerAccId) == null)
            throw new AccountNotFoundException("No such account found");
        if (!manager.checkDepositName(name, ownerAccId))
            throw new DepositNameExistingException("Deposit with this name already existing!");
        if (!manager.checkAmountAtAccount(amount, ownerAccId))
            throw new NotEnoughFundsException("Not enough funds at account!");
        manager.createDeposit(this);
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "depositId=" + depositId +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", rate=" + rate +
                ", ownerAccId=" + ownerAccId +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
