package banking_app.classes;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoanTest {

    @Test
    void getLoanId() {
        Date start = Date.valueOf("2024-01-01"), end = Date.valueOf("2025-02-02");
        BigDecimal amount = new BigDecimal("1000"), rate = new BigDecimal("0.1");
        double fixed = LoanCalc.calculateLoanMonthly(amount.doubleValue(), 1, rate.doubleValue());
        Loan loan = new Loan(81, amount, start, rate, end, 1000000000000000046L, new BigDecimal(fixed));
        assert loan.getLoanId() == 81;
    }

    @Test
    void getAmount() {
        Date start = Date.valueOf("2024-01-01"), end = Date.valueOf("2025-02-02");
        BigDecimal amount = new BigDecimal("1000"), rate = new BigDecimal("0.1");
        double fixed = LoanCalc.calculateLoanMonthly(amount.doubleValue(), 1, rate.doubleValue());
        Loan loan = new Loan(81, amount, start, rate, end, 1000000000000000046L, new BigDecimal(fixed));
        assert loan.getAmount().equals(new BigDecimal("1000"));
    }

    @Test
    void getStart() {
        Date start = Date.valueOf("2024-01-01"), end = Date.valueOf("2025-02-02");
        BigDecimal amount = new BigDecimal("1000"), rate = new BigDecimal("0.1");
        double fixed = LoanCalc.calculateLoanMonthly(amount.doubleValue(), 1, rate.doubleValue());
        Loan loan = new Loan(81, amount, start, rate, end, 1000000000000000046L, new BigDecimal(fixed));
        Date compareDate = Date.valueOf("2024-01-01");
        assert loan.getStart().equals(compareDate);
    }

    @Test
    void getEnd() {
        Date start = Date.valueOf("2024-01-01"), end = Date.valueOf("2025-02-02");
        BigDecimal amount = new BigDecimal("1000"), rate = new BigDecimal("0.1");
        double fixed = LoanCalc.calculateLoanMonthly(amount.doubleValue(), 1, rate.doubleValue());
        Loan loan = new Loan(81, amount, start, rate, end, 1000000000000000046L, new BigDecimal(fixed));
        Date compareDate = Date.valueOf("2025-02-02");
        assert loan.getEnd().equals(compareDate);
    }

    @Test
    void getRate() {
        Date start = Date.valueOf("2024-01-01"), end = Date.valueOf("2025-02-02");
        BigDecimal amount = new BigDecimal("1000"), rate = new BigDecimal("0.1");
        double fixed = LoanCalc.calculateLoanMonthly(amount.doubleValue(), 1, rate.doubleValue());
        Loan loan = new Loan(81, amount, start, rate, end, 1000000000000000046L, new BigDecimal(fixed));
        BigDecimal compareRate = new BigDecimal("0.1");
        assert loan.getRate().equals(compareRate);
    }

    @Test
    void getOwnerAccId() {
        Date start = Date.valueOf("2024-01-01"), end = Date.valueOf("2025-02-02");
        BigDecimal amount = new BigDecimal("1000"), rate = new BigDecimal("0.1");
        double fixed = LoanCalc.calculateLoanMonthly(amount.doubleValue(), 1, rate.doubleValue());
        Loan loan = new Loan(81, amount, start, rate, end, 1000000000000000046L, new BigDecimal(fixed));
        assert loan.getOwnerAccId() == 1000000000000000046L;
    }

    @Test
    void getFixed() {
        Date start = Date.valueOf("2024-01-01"), end = Date.valueOf("2025-02-02");
        BigDecimal amount = new BigDecimal("1000"), rate = new BigDecimal("0.1");
        double fixed = LoanCalc.calculateLoanMonthly(amount.doubleValue(), 1, rate.doubleValue());
        Loan loan = new Loan(81, amount, start, rate, end, 1000000000000000046L, new BigDecimal(fixed));
        double otherFixed =  LoanCalc.calculateLoanMonthly(amount.doubleValue(), 1, rate.doubleValue());
        assert loan.getFixed().doubleValue() == otherFixed;
    }

    @Test
    void testCreateWrongAmount() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        NumberFormatException thrown = assertThrows(NumberFormatException.class,
                ()->{
            Loan.createLoan(null, "1000a", "10", "2025-01-01", "1000000000000046", user);
        });
        assertEquals("For input string: \"1000a\"", thrown.getMessage());
    }


    @Test
    void testCreateNegativeAmount() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        NumberFormatException thrown = assertThrows(NumberFormatException.class,
                ()->{
                    Loan.createLoan(null, "-1", "10", "2025-01-01", "1000000000000046", user);
                });
        assertEquals("Amount less then 0", thrown.getMessage());
    }
    @Test
    void testCreateWrongRate() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        NumberFormatException thrown = assertThrows(NumberFormatException.class,
                ()->{
                    Loan.createLoan(null, "1000", "10a", "2025-01-01", "1000000000000046", user);
                });
        assertEquals("For input string: \"10a\"", thrown.getMessage());
    }
    @Test
    void testCreateDateAmount() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                ()->{
                    Loan.createLoan(null, "1000", "10", "2025-01-011", "1000000000000046", user);
                });
        assertEquals(null, thrown.getMessage());
    }
    @Test
    void testNegativeRate() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        NumberFormatException thrown = assertThrows(NumberFormatException.class,
                ()->{
                    Loan.createLoan(null, "1000", "-10", "2025-01-01", "1000000000000046", user);
                });
        assertEquals("Rate less then 0", thrown.getMessage());
    }
    @Test
    void testShortOwnerAcc() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        NumberFormatException thrown = assertThrows(NumberFormatException.class,
                ()->{
                    Loan.createLoan(null, "1000", "10", "2025-01-01", "100000000000004", user);
                });
        assertEquals("Short id", thrown.getMessage());
    }

}