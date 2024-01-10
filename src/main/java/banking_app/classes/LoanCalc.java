package banking_app.classes;

public class LoanCalc {
    public LoanCalc(){}

    public static double calculateLoanMonthly(double amount, int loanTerm, double rate) throws NumberFormatException {
        if (amount <= 0)
            throw new NumberFormatException();
        if (rate <= 0)
            throw new NumberFormatException();


        double outcome, r = rate / 1200;
        int n = loanTerm * 12;
        outcome = ((Math.pow((1+r), n) * amount * r) / (Math.pow((1+r), n) - 1));
        outcome *= 100;
        outcome = Math.round(outcome);
        return outcome / 100;
    }

    public static double calculateLoanMonthly(String amount, String loanTerm, String rate)
            throws NumberFormatException {
        double a, r;
        int l;

        a = Double.parseDouble(amount);
        r = Double.parseDouble(rate);
        l = Integer.parseInt(loanTerm);
        if (a <= 0)
            throw new NumberFormatException("Amount must be positive");
        if (r <= 0)
            throw new NumberFormatException("Rate must be positive");

        if (r > 20 || r < 0)
            throw new NumberFormatException();
        return calculateLoanMonthly(a, l ,r);
    }

    public static double calculateLoanTotal(double amount, int loanTerm, double rate) {
        double monthly = calculateLoanMonthly(amount, loanTerm, rate);
        double total = monthly * 12 * loanTerm;
        total *= 100;
        total = Math.round(total);
        return total / 100;
    }

    public static double calculateLoanTotal(String amount, String loanTerm, String rate) {
        double monthly = calculateLoanMonthly(amount, loanTerm, rate);

        int l;
        l = Integer.parseInt(loanTerm);
        double total = monthly * 12 * l;
        total *= 100;
        total = Math.round(total);
        return total / 100;
    }
}
