package banking_app.classes;

public class AutomaticSaving {
    private final int savingID;
    private String name;
    private final int dayStarted;
    private final int monthStarted;
    private final int yearStarted;
    private int sourceAccountId;
    private int targetAccountId;

    public AutomaticSaving(int savingID, String name, int dayStarted,
                           int monthStarted, int yearStarted,
                           int sourceAccountId, int targetAccountId) {
        this.savingID = savingID;
        this.name = name;
        this.dayStarted = dayStarted;
        this.monthStarted = monthStarted;
        this.yearStarted = yearStarted;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
    }

    public int getSavingID() {
        return savingID;
    }

    public String getName() {
        return name;
    }

    public int getDayStarted() {
        return dayStarted;
    }

    public int getMonthStarted() {
        return monthStarted;
    }

    public int getYearStarted() {
        return yearStarted;
    }

    public int getSourceAccountId() {
        return sourceAccountId;
    }

    public int getTargetAccountId() {
        return targetAccountId;
    }
}
