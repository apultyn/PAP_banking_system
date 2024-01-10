package banking_app.classes;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.*;

class DepositTest {

    @Test
    void getDepositId() {
        Deposit deposit = new Deposit(1000, "Deposit Name",
                new BigDecimal(1000), new BigDecimal(5), 1000000000000023L,
                Date.valueOf("2024-01-08"), Date.valueOf("2024-03-08"));
        assert deposit.getDepositId() == 1000;
    }

    @Test
    void getName() {
        Deposit deposit = new Deposit(1000, "Deposit Name",
                new BigDecimal(1000), new BigDecimal(5), 1000000000000023L,
                Date.valueOf("2024-01-08"), Date.valueOf("2024-03-08"));
        assert deposit.getName().equals("Deposit Name");
    }

    @Test
    void getAmount() {
        Deposit deposit = new Deposit(1000, "Deposit Name",
                new BigDecimal(1000), new BigDecimal(5), 1000000000000023L,
                Date.valueOf("2024-01-08"), Date.valueOf("2024-03-08"));
        assert deposit.getAmount().compareTo(new BigDecimal(1000)) == 0;
    }

    @Test
    void getRate() {
        Deposit deposit = new Deposit(1000, "Deposit Name",
                new BigDecimal(1000), new BigDecimal(5), 1000000000000023L,
                Date.valueOf("2024-01-08"), Date.valueOf("2024-03-08"));
        assert deposit.getRate().compareTo(new BigDecimal(5)) == 0;
    }

    @Test
    void getOwnerAccId() {
        Deposit deposit = new Deposit(1000, "Deposit Name",
                new BigDecimal(1000), new BigDecimal(5), 1000000000000023L,
                Date.valueOf("2024-01-08"), Date.valueOf("2024-03-08"));
        assert deposit.getOwnerAccId() == 1000000000000023L;
    }

    @Test
    void getStart() {
        Deposit deposit = new Deposit(1000, "Deposit Name",
                new BigDecimal(1000), new BigDecimal(5), 1000000000000023L,
                Date.valueOf("2024-01-08"), Date.valueOf("2024-03-08"));
        assert deposit.getStart().equals(Date.valueOf("2024-01-08"));
    }

    @Test
    void getEnd() {
        Deposit deposit = new Deposit(1000, "Deposit Name",
                new BigDecimal(1000), new BigDecimal(5), 1000000000000023L,
                Date.valueOf("2024-01-08"), Date.valueOf("2024-03-08"));
        assert deposit.getEnd().equals(Date.valueOf("2024-03-08"));
    }

    @Test
    void createDepositError() {
        Deposit deposit = new Deposit(1000, "Deposit Name",
                new BigDecimal(1000), new BigDecimal(5), 1000000000000023L,
                Date.valueOf("2024-01-08"), Date.valueOf("2024-01-03"));
        DateTimeException thrown = assertThrows(DateTimeException.class, ()->{
            deposit.createDeposit(null);
        });
    }
}