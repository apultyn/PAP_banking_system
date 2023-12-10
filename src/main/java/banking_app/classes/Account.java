package banking_app.classes;

public class Account {
    private final int accountId;
    private String name;
    private float dialyLimit;
    private final int dayCreated;
    private final int monthCreated;
    private final int yearCreated;
    private final int userId;

    public Account(int accountId, String name, float dialyLimit,
                   int dayCreated, int monthCreated,
                   int yearCreated, int userId) {
        this.accountId = accountId;
        this.name = name;
        this.dialyLimit = dialyLimit;
        this.dayCreated = dayCreated;
        this.monthCreated = monthCreated;
        this.yearCreated = yearCreated;
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public float getDialyLimit() {
        return dialyLimit;
    }

    public int getDayCreated() {
        return dayCreated;
    }

    public int getMonthCreated() {
        return monthCreated;
    }

    public int getYearCreated() {
        return yearCreated;
    }

    public int getUserId() {
        return userId;
    }
}
