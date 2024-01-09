package banking_app.classes;

import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StandingOrder {
    private final int orderId;
    private String name;
    private Date dateStarted;
    private long sourceAccountId;
    private long targetAccountId;

    private BigDecimal amount;

    public StandingOrder(int savingID, String name, Date started, long sender_id, long reciever_id, BigDecimal am) {
        this.orderId = savingID;
        this.name = name;
        this.dateStarted = started;
        this.sourceAccountId = sender_id;
        this.targetAccountId = reciever_id;
        this.amount = am;
    }

    public StandingOrder(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt("order_id"),
                resultSet.getString("name"),
                resultSet.getDate("start_date"),
                resultSet.getLong("sender_id"),
                resultSet.getLong("reciever_id"),
                resultSet.getBigDecimal("amount"));
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

    public static void registerStandingOrder(ConnectionManager manager, User user, String name, String  senderId, String recieverId, String howMuch)
        throws SQLException, NumberFormatException{
        List<Account> accounts = manager.findUsersAccounts(user.getId());
        if (senderId.equals(recieverId))
            throw new NumberFormatException("The same accounts");
        long sender, reciever;
        BigDecimal amount;
        if (senderId.length() != 16 || recieverId.length() != 16)
            throw new NumberFormatException("Short id");
        sender = Long.parseLong(senderId);
        reciever = Long.parseLong(recieverId);

        boolean senderIsMy= false;
        for (Account a:accounts) {
            if (a.getAccountId() == sender)
                senderIsMy = true;
        }
        if (!senderIsMy)
            throw new NumberFormatException("Not my accounts");

        amount = new BigDecimal(howMuch);
        manager.createStadningOrder(name, amount, sender, reciever);
    }

}
