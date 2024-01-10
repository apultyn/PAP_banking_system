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

public class StandingOrder {
    private final Integer orderId;
    private final String name;
    private final Date dateStarted;
    private final long sourceAccountId;
    private final long targetAccountId;

    private final BigDecimal amount;

    public StandingOrder(int savingID, String name, Date started, long sender_id, long receiver_id, BigDecimal am) {
        this.orderId = savingID;
        this.name = name;
        this.dateStarted = started;
        this.sourceAccountId = sender_id;
        this.targetAccountId = receiver_id;
        this.amount = am;
    }

    public StandingOrder(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt("order_id"),
                resultSet.getString("name"),
                resultSet.getDate("start_date"),
                resultSet.getLong("sender_id"),
                resultSet.getLong("receiver_id"),
                resultSet.getBigDecimal("amount"));
    }

    public StandingOrder(String name, String amount, String senderAccountNumber, String recipientAccountNumber) throws InvalidNameException, InvalidAmountException, InvalidAccountNumberException {
        if (name.isEmpty())
            throw new InvalidNameException("Name cannot be empty!");
        if (amount.isEmpty())
            throw new InvalidAmountException("Amount cannot be empty!");
        if (!isBigDecimal(amount))
            throw new InvalidAmountException("Amount must be a number!");
        if (new BigDecimal(amount).compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Amount must be positive!");
        if (!recipientAccountNumber.matches("\\d{16}"))
            throw new InvalidAccountNumberException("Number must be 16 digits long!");
        if (senderAccountNumber.equals(recipientAccountNumber))
            throw new InvalidAccountNumberException("Accounts must be different!");
        this.orderId = null;
        this.name = name;
        this.dateStarted = null;
        this.sourceAccountId = Long.parseLong(senderAccountNumber);
        this.targetAccountId = Long.parseLong(recipientAccountNumber);
        this.amount = new BigDecimal(amount);

    }

    public int getSavingID() {
        return orderId;
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

//    public static void registerStandingOrder(ConnectionManager manager, User user, String name, String  senderId, String recieverId, String howMuch)
//        throws SQLException, NumberFormatException{
//        List<Account> accounts = manager.findUsersAccounts(user.getId());
//        if (senderId.equals(recieverId))
//            throw new NumberFormatException("The same accounts");
//        long sender, reciever;
//        BigDecimal amount;
//        if (senderId.length() != 16 || recieverId.length() != 16)
//            throw new NumberFormatException("Short id");
//        sender = Long.parseLong(senderId);
//        reciever = Long.parseLong(recieverId);
//
//        boolean senderIsMy= false;
//        for (Account a:accounts) {
//            if (a.getAccountId() == sender)
//                senderIsMy = true;
//        }
//        if (!senderIsMy)
//            throw new NumberFormatException("Not my accounts");
//
//        amount = new BigDecimal(howMuch);
//        manager.createStadningOrder(name, amount, sender, reciever);
//    }

    public static void registerStandingOrder(ConnectionManager manager, StandingOrder standingOrder) throws InvalidNameException, InvalidAmountException, InvalidAccountNumberException, SQLException {
        if (manager.findAccount(standingOrder.getSourceAccountId()) == null)
            throw new InvalidAccountNumberException("Account not existing!");
        manager.createStandingOrder(standingOrder);
    }

}
