package banking_app.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoanCalcTest {

    @Test
    void calculateLoanMonthly() {
        double rate = 4, amount = 1000;
        int loanTerm = 2;
        double monthly = LoanCalc.calculateLoanMonthly("1000", "2", "4");
        double outcome, r = rate / 1200;
        int n = loanTerm * 12;
        outcome = ((Math.pow((1+r), n) * amount * r) / (Math.pow((1+r), n) - 1));
        outcome *= 100;
        outcome = Math.round(outcome);
        outcome /= 100;
        assert monthly == outcome;
    }

    @Test
    void calculateLoanTotal() {
        double rate = 4, amount = 1000;
        int loanTerm = 2;
        double total = LoanCalc.calculateLoanTotal("1000", "2", "4");
        double outcome, r = rate / 1200;
        int n = loanTerm * 12;
        outcome = ((Math.pow((1+r), n) * amount * r) / (Math.pow((1+r), n) - 1));
        outcome *= 100;
        outcome = Math.round(outcome);
        outcome /= 100;
        assert total == outcome * 24;
    }

    @Test
    void wrongAmountCalc() {
        NumberFormatException thrown = assertThrows(NumberFormatException.class,
                ()->{
                    LoanCalc.calculateLoanMonthly("1000a", "10", "2");
                });
        assertEquals("For input string: \"1000a\"", thrown.getMessage());
    }

    @Test
    void negativeAmountCalc() {
        NumberFormatException thrown = assertThrows(NumberFormatException.class,
                ()->{
                    LoanCalc.calculateLoanMonthly("-1", "10", "2");
                });
        assertEquals("Amount must be positive", thrown.getMessage());
    }
    @Test
    void negativeRateCalc() {
        NumberFormatException thrown = assertThrows(NumberFormatException.class,
                ()->{
                    LoanCalc.calculateLoanMonthly("100", "10", "-2");
                });
        assertEquals("Rate must be positive", thrown.getMessage());
    }

    @Test
    void wrongRateCalc() {
        NumberFormatException thrown = assertThrows(NumberFormatException.class,
                ()->{
                    LoanCalc.calculateLoanMonthly("100", "10", "2a");
                });
        assertEquals("For input string: \"2a\"", thrown.getMessage());
    }
    @Test
    void bigRateCalc() {
        NumberFormatException thrown = assertThrows(NumberFormatException.class,
                ()->{
                    LoanCalc.calculateLoanMonthly("100", "10", "200");
                });
        assertEquals(null, thrown.getMessage());
    }
}