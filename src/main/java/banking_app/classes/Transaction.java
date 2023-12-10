package banking_app.classes;

import connections.ConnectionManager;

import java.sql.Date;
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
    }
    public int getType() {
        return type;
    }

    public static Transaction getFromConsole(ConnectionManager connectionManager, Account sender) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj nr rachunku odbiorcy otzymujcego");
        int reciever_acc_id = Integer.parseInt(sc.next());
        System.out.println("Podaj kwote do przelania");
        float amount = Float.parseFloat( sc.next());
        System.out.println("Tytul");
        String title = sc.next();
        long mils = System.currentTimeMillis();
        Date date = new Date(mils);
        Transaction newTransaction = new Transaction(sender.getUserId(), reciever_acc_id, date, amount, 1, title);
        registerTransaction(connectionManager, newTransaction);
        return newTransaction;
    }
    public static void registerTransaction(ConnectionManager connectionManager, Transaction newTransaction) throws SQLException {
        connectionManager.registerTransaction(newTransaction);
    }
}
