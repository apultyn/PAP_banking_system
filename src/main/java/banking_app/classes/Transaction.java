package banking_app.classes;

import java.sql.Connection;

public class Transaction
{
    private int transactionId, day, month, year, sourceId, targetId;
    private float amount;
    private char type;
    private String title;

    public Transaction(int transactionId, int day, int month,
                       int year, int sourceId, int targetId,
                       float amount, char type, String title){
        this.transactionId = transactionId;
        this.year = year;
        this.day = day;
        this.month = month;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.amount = amount;
        this.type = type;
        this.title = title;
    }
    public int getTransactionId(){
        return transactionId;
    }
    public int getDay() {
        return day;
    }
    public int getMonth() {
        return month;
    }
    public int getYear() {
        return year;
    }
    public int getSourceId() {
        return sourceId;
    }
    public int getTargetId() {
        return targetId;
    }
    public float getAmount() {
        return amount;
    }
    public String getTitle() {
        return title;
    }
    public char getType() {
        return type;
    }
    public String registerTransaction(Connection connection) {
        //save to bd
        return "Successfuly regirested transaction" + transactionId;
    }

}
