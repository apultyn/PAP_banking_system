package banking_app.classes;

import banking_exceptions.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @Test
    void updateTransferLimitEmpty() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000.02),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, ()->{
            account.updateTransferLimit(null, "");
        });
        assertEquals("Transfer limit cannot be empty!", thrown.getMessage());
    }

    @Test
    void updateTransferLimitNotNumber() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000.02),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, ()->{
            account.updateTransferLimit(null, "200a");
        });
        assertEquals("Transfer limit must be a number!", thrown.getMessage());
    }

    @Test
    void updateTransferLimitNegative() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000.02),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, ()->{
            account.updateTransferLimit(null, "-2093.09");
        });
        assertEquals("Transfer limit must be positive!", thrown.getMessage());
    }


    @Test
    void getAccountId() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        assert account.getAccountId() == 1000;
    }

    @Test
    void getName() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        assert account.getName().equals("Main acc");
    }

    @Test
    void getTransferLimit() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000.02),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        assert account.getTransferLimit().compareTo(new BigDecimal(1000.02)) == 0;
    }

    @Test
    void getDateCreated() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        assert account.getDateCreated().equals(Date.valueOf("2024-01-02"));
    }

    @Test
    void getUserId() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        assert account.getUserId() == 109;
    }

    @Test
    void getBalance() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        assert account.getBalance().compareTo(new BigDecimal(2000)) == 0;
    }

    @Test
    void setUserId() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        account.setUserId(108);
        assert account.getUserId() == 108;
    }

    @Test
    void setTransferLimit() {
        Account account = new Account(1000, "Main acc", new BigDecimal(1000.02),
                Date.valueOf("2024-01-02"), 109, new BigDecimal(2000));
        account.setTransferLimit(new BigDecimal("2000.90"));
        assert account.getTransferLimit().compareTo(new BigDecimal("2000.90")) == 0;
    }

}