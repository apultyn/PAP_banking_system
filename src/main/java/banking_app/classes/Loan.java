package banking_app.classes;

import connections.ConnectionManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Loan {
    private final Integer loanId;
    private final BigDecimal amount;
    private final BigDecimal rate;
    private final long ownerAccId;
    private final Date start;
    private final Date end;
    BigDecimal fixed;

    public Loan(int loanId, BigDecimal amount, Date start,
                   BigDecimal rate, Date end, long ownerId, BigDecimal fixed) {
        this.loanId = loanId;
        this.rate = rate;
        this.amount = amount;
        this.ownerAccId = ownerId;
        this.start = start;
        this.end = end;
        this.fixed = fixed;
    }

    public Loan(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt("loan_id"),
                resultSet.getBigDecimal("amount"),
                resultSet.getDate("start_date"),
                resultSet.getBigDecimal("rate"),
                resultSet.getDate("finish_date"),
                resultSet.getLong("owner_acc_id"),
                resultSet.getBigDecimal("fixed_rate"));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public Date getEnd() {
        return end;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public Date getStart() {
        return start;
    }

    public long getOwnerAccId() {
        return ownerAccId;
    }

    public BigDecimal getFixed() {
        return fixed;
    }

    public static void createLoan(ConnectionManager manager, BigDecimal amount,
                                  BigDecimal rate, Date end, long ownerId) throws SQLException, NumberFormatException {
        if (rate.doubleValue() > 20d || rate.doubleValue() < 0)
            throw new NumberFormatException("Wrong rate");

        java.util.Date current = new java.util.Date();
        int yearDiff = (end.getYear() - (new java.sql.Date(current.getTime()).getYear()));

        if (yearDiff <= 0)
            throw new NumberFormatException("Wrong date");
        if (amount.doubleValue() <= 0)
            throw new NumberFormatException("Amount less then 0");
        if (rate.doubleValue() <= 0)
            throw new NumberFormatException("Rate less then 0");

        double fixedRate = LoanCalc.calculateLoanMonthly(amount.doubleValue(), yearDiff, rate.doubleValue()), calculatedAmountL;
        BigDecimal finalFixedRate = new BigDecimal(fixedRate);
        finalFixedRate = finalFixedRate.setScale(2, RoundingMode.HALF_UP);
        calculatedAmountL = fixedRate * Double.valueOf(yearDiff) * Double.valueOf(12);
        calculatedAmountL *= 100;
        calculatedAmountL = Math.round(calculatedAmountL);
        calculatedAmountL /= 100;
        BigDecimal newAmount = new BigDecimal(calculatedAmountL);
        manager.createLoan(newAmount, rate, end, ownerId, finalFixedRate);
    }

    public static void createLoan(ConnectionManager manager, String amount, String rate,
                                  String end, String ownerAccId, User user) throws SQLException, IllegalArgumentException {
        if (ownerAccId.length() != 16)
            throw new NumberFormatException("Short id");
        double a = Double.parseDouble(amount), r = Double.parseDouble(rate);
        BigDecimal amountBig = BigDecimal.valueOf(a), rateBig = BigDecimal.valueOf(r);
        long owner = Long.parseLong(ownerAccId);
        if (a <= 0)
            throw new NumberFormatException("Amount less then 0");
        if (r <= 0)
            throw new NumberFormatException("Rate less then 0");
        Date e = Date.valueOf(end);
        List<Account> accounts = manager.findUsersAccounts(user.getId());

        boolean myAccount = false;
        for (Account acc : accounts) {
            if (acc.getAccountId() == owner)
                myAccount = true;
        }
        if (!myAccount)
            throw new NumberFormatException("Not mine");

        createLoan(manager, amountBig, rateBig, e, owner);
    }
}
