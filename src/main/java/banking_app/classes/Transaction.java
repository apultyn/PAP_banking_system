package banking_app.classes;

import connections.ConnectionManager;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Transaction
{
    private int transactionId, sourceId, targetId;
    private float amount;
    private int type;
    private String title;

    private Date date;

    public Transaction(int transactionId, int sourceId, int targetId,
                       Date date, float amount, int type, String title){
        this.transactionId = transactionId;
        this.date = date;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.amount = amount;
        this.type = type;
        this.title = title;
    }
    public Transaction(int sourceId, int targetId, Date date, float amount, int type, String title)
    {
        this.date = date;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.amount = amount;
        this.type = type;
        this.title = title;
    }


    public Transaction(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt("transaction_id"),
                resultSet.getInt("sender_id"),
                resultSet.getInt("target_id"),
                resultSet.getDate("date_made"),
                resultSet.getFloat("amount"),
                resultSet.getInt("type"),
                resultSet.getString("title"));
    }

    public int getTransactionId(){
        return transactionId;
    }
    public int getSourceId() {
        return sourceId;
    }
    public int getTargetId() {
        return targetId;
    }
    public Date getDate() {
        return date;
    }
    public float getAmount() {
        return amount;
    }
    public String getTitle() {
        return title;
        //abc
    }
    public int getType() {
        return type;
    }


    public static boolean isValidAccountId(ConnectionManager connectionManager, int accountId) throws SQLException{
        return connectionManager.findAccount(accountId) != null;
    }

    public static void getFromConsole(ConnectionManager connectionManager, Account sender) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int reciever_acc_id;
        do {
            System.out.println("Podaj nr rachunku odbiorcy otzymujcego");
            reciever_acc_id = Integer.parseInt(sc.next());
        } while (isValidAccountId(connectionManager, reciever_acc_id));
        float amount;
        do {
            System.out.println("Podaj kwote do przelania");
            amount = Float.parseFloat(sc.next());
        } while (amount < 0 || sender.getTransactionLimit() < amount);
        System.out.println("Tytul");
        String title = sc.next();
        connectionManager.registerTransaction(title, amount, 1, sender.getAccountId(), reciever_acc_id);
    }

}
