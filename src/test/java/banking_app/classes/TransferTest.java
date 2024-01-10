package banking_app.classes;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    @Test
    void getTransferId() {
        Transfer transfer = new Transfer(1000, 100000000023L,
                1000000000000098L, Date.valueOf("2024-01-04"), new BigDecimal("200.99"),
                1,"Title");
        assert transfer.getTransferId() == 1000;
    }

    @Test
    void getSourceId() {
        Transfer transfer = new Transfer(1000, 100000000023L,
                1000000000000098L, Date.valueOf("2024-01-04"), new BigDecimal("200.99"),
                1,"Title");
        assert transfer.getSourceId() == 100000000023L;
    }

    @Test
    void getTargetId() {
        Transfer transfer = new Transfer(1000, 100000000023L,
                1000000000000098L, Date.valueOf("2024-01-04"), new BigDecimal("200.99"),
                1,"Title");
        assert transfer.getTargetId() == 1000000000000098L;
    }

    @Test
    void getDate() {
        Transfer transfer = new Transfer(1000, 100000000023L,
                1000000000000098L, Date.valueOf("2024-01-04"), new BigDecimal("200.99"),
                1,"Title");
        assert transfer.getDate().equals(Date.valueOf("2024-01-04"));
    }

    @Test
    void getAmount() {
        Transfer transfer = new Transfer(1000, 100000000023L,
                1000000000000098L, Date.valueOf("2024-01-04"), new BigDecimal("200.99"),
                1,"Title");
        assert transfer.getAmount().compareTo(new BigDecimal("200.99")) == 0;
    }

    @Test
    void getTitle() {
        Transfer transfer = new Transfer(1000, 100000000023L,
                1000000000000098L, Date.valueOf("2024-01-04"), new BigDecimal("200.99"),
                1,"Title");
        assert transfer.getTitle().equals("Title");
    }

    @Test
    void getType() {
        Transfer transfer = new Transfer(1000, 100000000023L,
                1000000000000098L, Date.valueOf("2024-01-04"), new BigDecimal("200.99"),
                1,"Title");
        assert transfer.getType() == 1;
    }

}