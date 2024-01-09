package banking_app.classes;

import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    public static void registerAutomaticSaving(ConnectionManager connection, User user, String name, String senderId, String recieverId, String howMuch)
            throws NumberFormatException, SQLException {
        List<Account> accounts = connection.findUsersAccounts(user.getId());
        if (senderId.equals(recieverId))
            throw new NumberFormatException("The same accounts");
        long sender, reciever;
        BigDecimal amount;
        if (senderId.length() != 16 || recieverId.length() != 16)
            throw new NumberFormatException("Short id");
        sender = Long.parseLong(senderId);
        reciever = Long.parseLong(recieverId);

        boolean recieverIsMy = false, senderIsMy= false;
        for (Account a:accounts) {
            if (a.getAccountId() == sender)
                senderIsMy = true;
            if (a.getAccountId() == reciever)
                recieverIsMy = true;
        }

        if (!senderIsMy || !recieverIsMy)
            throw new NumberFormatException("Not my accounts");
        amount = new BigDecimal(howMuch);
        connection.createAutomaticSaving(name, sender, reciever, amount);
    }
}
