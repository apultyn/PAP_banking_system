package banking_app.classes;

import banking_exceptions.AccountNotFoundException;
import banking_exceptions.DepositNameExistingException;
import banking_exceptions.NotEnoughFundsException;
import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Deposit {
    private final int depositId;
    private String name;
    private BigDecimal amount;
    private BigDecimal rate;
    private long ownerAccId;
    private Date start, end;

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
        return  end;
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

    public static void main(String[] args) throws SQLException {
        ConnectionManager manager = new ConnectionManager();
        Deposit deposit1 = new Deposit(0, "Pierwsze", new BigDecimal(0.1), new BigDecimal(5), 1000000000000046L,
                new Date(2023, 1, 8), new Date(2023, 1, 9));
        Deposit deposit2 = new Deposit(0, "Drugie", new BigDecimal(0.1), new BigDecimal(5), 1000000000000062L,
                new Date(2023, 1, 8), new Date(2023, 1, 9));
        Deposit deposit3 = new Deposit(0, "Trzecie", new BigDecimal(0.1), new BigDecimal(5), 1000000000000063L,
                new Date(2023, 1, 8), new Date(2023, 1, 9));
        manager.createDeposit(deposit2);
        manager.createDeposit(deposit3);
        ArrayList<Deposit> list = new ArrayList<>(manager.findUsersDeposits(85));
        for (Deposit deposit : list) {
            System.out.println(deposit);
        }
    }
}
