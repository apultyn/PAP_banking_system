package banking_app.classes;

import banking_exceptions.AccountNotFoundException;
import banking_exceptions.DepositNameExistingException;
import banking_exceptions.NotEnoughFundsException;
import connections.ConnectionManager;

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
    private Date start;
    private Date end;

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
        if (manager.findAccount(ownerAccId) == null)
            throw new AccountNotFoundException("No such account found");
        if (!manager.checkDepositName(name, ownerAccId))
            throw new DepositNameExistingException("Deposit with this name already existing!");
        if (!manager.checkAmountAtAccount(amount, ownerAccId))
            throw new NotEnoughFundsException("Not enough funds at account!");
        manager.createDeposit(name, amount, rate, ownerAccId, end);
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
