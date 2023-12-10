package banking_app.classes;

public class Deposit {
    private final int depositId;
    private String name;
    private int amount;
    private float rate;
    private int ownerId;
    private final int dayStarted;
    private final int monthStarted;
    private final int yearStarted;
    private final int dayEnd;
    private final int monthEnd;
    private final int yearEnd;

    public Deposit(int depositId, String name, int amount,
                   float rate, int ownerId, int dayStarted,
                   int monthStarted, int yearStarted, int dayEnd,
                   int monthEnd, int yearEnd) {
        this.depositId = depositId;
        this.name = name;
        this.amount = amount;
        this.rate = rate;
        this.ownerId = ownerId;
        this.dayStarted = dayStarted;
        this.monthStarted = monthStarted;
        this.yearStarted = yearStarted;
        this.dayEnd = dayEnd;
        this.monthEnd = monthEnd;
        this.yearEnd = yearEnd;
    }

    public int getDepositId() {
        return depositId;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public float getRate() {
        return rate;
    }

    public int getOwnerId() {
        return ownerId;
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

    public int getDayEnd() {
        return dayEnd;
    }

    public int getMonthEnd() {
        return monthEnd;
    }

    public int getYearEnd() {
        return yearEnd;
    }
}
