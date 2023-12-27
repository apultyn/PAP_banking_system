package banking_app.classes;

import java.sql.Date;

public class AutomaticSaving {
    private final int savingID;
    private String name;
    private Date dateStarted;
    private int sourceAccountId;
    private int targetAccountId;

    public AutomaticSaving(int savingID, String name, Date started,
                           int sourceAccountId, int targetAccountId) {
        this.savingID = savingID;
        this.name = name;
        this.dateStarted = started;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
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

    public int getSourceAccountId() {
        return sourceAccountId;
    }

    public int getTargetAccountId() {
        return targetAccountId;
    }
}
