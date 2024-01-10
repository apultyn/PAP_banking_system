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

public class AutomaticSaving {
    private final Integer savingID;
    private final String name;
    private final Date dateStarted;
    private final long sourceAccountId;
    private final long targetAccountId;

    private final BigDecimal amount;

    public AutomaticSaving(int savingID, String name, Date started, long sender_id, long receiver_id, BigDecimal am) {
        this.savingID = savingID;
        this.name = name;
        this.dateStarted = started;
        this.sourceAccountId = sender_id;
        this.targetAccountId = receiver_id;
        this.amount = am;
    }

    public AutomaticSaving(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt("saving_id"),
                resultSet.getString("name"),
                resultSet.getDate("start_date"),
                resultSet.getLong("sender_id"),
                resultSet.getLong("receiver_id"),
                resultSet.getBigDecimal("amount"));
    }

    public AutomaticSaving(String name, String amount, String senderAccountNumber, String recipientAccountNumber)
            throws InvalidNameException, InvalidAmountException, InvalidAccountNumberException {

        if (name.isEmpty())
            throw new InvalidNameException("Name cannot be empty!");
        if (amount.isEmpty())
            throw new InvalidAmountException("Amount cannot be empty!");
        if (!isBigDecimal(amount))
            throw new InvalidAmountException("Amount must be a number!");
        if (new BigDecimal(amount).compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Amount must be positive!");
        if (!recipientAccountNumber.matches("\\d{16}") || !senderAccountNumber.matches("\\d{16}"))
            throw new InvalidAccountNumberException("Number must be 16 digits long!");
        if (senderAccountNumber.equals(recipientAccountNumber))
            throw new InvalidAccountNumberException("Accounts must be different!");
        this.savingID = null;
        this.name = name;
        this.dateStarted = null;
        this.sourceAccountId = Long.parseLong(senderAccountNumber);
        this.targetAccountId = Long.parseLong(recipientAccountNumber);
        this.amount = new BigDecimal(amount);

    }

    public Integer getSavingID() {
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

/*    public static void registerAutomaticSaving(ConnectionManager connection, User user, String name, String senderId, String receiverId, String howMuch)
            throws NumberFormatException, SQLException {
        List<Account> accounts = connection.findUsersAccounts(user.getId());
        if (senderId.equals(receiverId))
            throw new NumberFormatException("The same accounts");
        long sender, receiver;
        BigDecimal amount;
        if (senderId.length() != 16 || receiverId.length() != 16)
            throw new NumberFormatException("Short id");
        sender = Long.parseLong(senderId);
        receiver = Long.parseLong(receiverId);

        boolean receiverIsMy = false, senderIsMy= false;
        for (Account a:accounts) {
            if (a.getAccountId() == sender)
                senderIsMy = true;
            if (a.getAccountId() == receiver)
                receiverIsMy = true;
        }

        if (!senderIsMy || !receiverIsMy)
            throw new NumberFormatException("Not my accounts");
        amount = new BigDecimal(howMuch);
        connection.createAutomaticSaving(name, sender, receiver, amount);
    }
*/
    public static void registerAutomaticSaving(ConnectionManager manager, AutomaticSaving automaticSaving) throws InvalidNameException, InvalidAmountException, InvalidAccountNumberException, SQLException {
        if (manager.findAccount(automaticSaving.getSourceAccountId()) == null)
            throw new InvalidAccountNumberException("Account not existing!");
        manager.createAutomaticSaving(automaticSaving);
    }
}
