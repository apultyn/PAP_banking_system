package banking_app.classes;

public class LoanCalc {
    public LoanCalc(){}

    public static double calculateLoanMonthly(double amount, int loanTerm, double rate) {
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
        if (r > 10 || r < 0)
            throw new NumberFormatException();
        return calculateLoanMonthly(a, l ,r);
    }
}
