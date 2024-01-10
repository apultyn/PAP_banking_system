package banking_app.classes;

import banking_exceptions.InvalidAccountNumberException;
import banking_exceptions.InvalidAmountException;
import banking_exceptions.InvalidNameException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutomaticSavingTest {

    @Test
    void getSavingID() {
        Date startDate = Date.valueOf("2024-01-10");
        AutomaticSaving automaticSaving = new AutomaticSaving(81, "My savings", startDate,
                1000000000000062L, 1000000000000046L, new BigDecimal(0.6));
        assert automaticSaving.getSavingID() == 81;
    }

    @Test
    void getName() {
        Date startDate = Date.valueOf("2024-01-10");
        AutomaticSaving automaticSaving = new AutomaticSaving(81, "My savings", startDate,
                1000000000000062L, 1000000000000046L, new BigDecimal(0.6));
        assert automaticSaving.getName().equals("My savings");
    }

    @Test
    void getDateStarted() {
        Date startDate = Date.valueOf("2024-01-10");
        AutomaticSaving automaticSaving = new AutomaticSaving(81, "My savings", startDate,
                1000000000000062L, 1000000000000046L, new BigDecimal(0.6));
        assert automaticSaving.getDateStarted().equals(startDate);
    }

    @Test
    void getSourceAccountId() {
        Date startDate = Date.valueOf("2024-01-10");
        AutomaticSaving automaticSaving = new AutomaticSaving(81, "My savings", startDate,
                1000000000000062L, 1000000000000046L, new BigDecimal(0.6));
        assert automaticSaving.getSourceAccountId() == 1000000000000062L;
    }

    @Test
    void getTargetAccountId() {
        Date startDate = Date.valueOf("2024-01-10");
        AutomaticSaving automaticSaving = new AutomaticSaving(81, "My savings", startDate,
                1000000000000062L, 1000000000000046L, new BigDecimal(0.6));
        assert automaticSaving.getTargetAccountId() == 1000000000000046L;
    }

    @Test
    void getAmount() {
        Date startDate = Date.valueOf("2024-01-10");
        AutomaticSaving automaticSaving = new AutomaticSaving(81, "My savings", startDate,
                1000000000000062L, 1000000000000046L, new BigDecimal(0.6));
        assert automaticSaving.getAmount().equals(new BigDecimal(0.6));
    }
    @Test
    void getSavingIDFromStr() {
        AutomaticSaving automaticSaving;
        try {
            automaticSaving = new AutomaticSaving("My savings", "0.6",
                    "1000000000000062", "1000000000000046");
        }  catch (InvalidNameException e) {
            throw new RuntimeException(e);
        } catch (InvalidAmountException e) {
            throw new RuntimeException(e);
        } catch (InvalidAccountNumberException e) {
            throw new RuntimeException(e);
        }
        assert automaticSaving.getSavingID() == null;
    }

    @Test
    void getNameFromStr() {
        AutomaticSaving automaticSaving;
        try {
            automaticSaving = new AutomaticSaving("My savings", "0.6",
                    "1000000000000062", "1000000000000046");
        }  catch (InvalidNameException e) {
            throw new RuntimeException(e);
        } catch (InvalidAmountException e) {
            throw new RuntimeException(e);
        } catch (InvalidAccountNumberException e) {
            throw new RuntimeException(e);
        }
        assert automaticSaving.getName().equals("My savings");
    }

    @Test
    void getDateStartedFromStr() {
        AutomaticSaving automaticSaving;
        try {
            automaticSaving = new AutomaticSaving("My savings", "0.6",
                    "1000000000000062", "1000000000000046");
        }  catch (InvalidNameException e) {
            throw new RuntimeException(e);
        } catch (InvalidAmountException e) {
            throw new RuntimeException(e);
        } catch (InvalidAccountNumberException e) {
            throw new RuntimeException(e);
        }
        assert automaticSaving.getDateStarted() == null;
    }

    @Test
    void getSourceAccountIdFromStr() {
        AutomaticSaving automaticSaving;
        try {
            automaticSaving = new AutomaticSaving("My savings", "0.6",
                    "1000000000000062", "1000000000000046");
        }  catch (InvalidNameException e) {
            throw new RuntimeException(e);
        } catch (InvalidAmountException e) {
            throw new RuntimeException(e);
        } catch (InvalidAccountNumberException e) {
            throw new RuntimeException(e);
        }
        assert automaticSaving.getSourceAccountId() == 1000000000000062L;
    }

    @Test
    void getTargetAccountIdFromString() {
        AutomaticSaving automaticSaving;
        try {
            automaticSaving = new AutomaticSaving("My savings", "0.6",
                    "1000000000000062", "1000000000000046");
        }  catch (InvalidNameException e) {
            throw new RuntimeException(e);
        } catch (InvalidAmountException e) {
            throw new RuntimeException(e);
        } catch (InvalidAccountNumberException e) {
            throw new RuntimeException(e);
        }
        assert automaticSaving.getTargetAccountId() == 1000000000000046L;
    }

    @Test
    void getAmountFromStr() {
        AutomaticSaving automaticSaving;
        try {
            automaticSaving = new AutomaticSaving("My savings", "0.6",
                    "1000000000000062", "1000000000000046");
        }  catch (InvalidNameException e) {
            throw new RuntimeException(e);
        } catch (InvalidAmountException e) {
            throw new RuntimeException(e);
        } catch (InvalidAccountNumberException e) {
            throw new RuntimeException(e);
        }
        BigDecimal toCompare = new BigDecimal("0.6");
        assert automaticSaving.getAmount().compareTo(toCompare) == 0;
    }

    @Test
    void createNameInvalid() {
        InvalidNameException thrown = assertThrows(InvalidNameException.class,
                ()->{
                    AutomaticSaving automaticSaving = new AutomaticSaving("", "0.6",
                            "1000000000000062", "1000000000000046");
                });
        assertEquals("Name cannot be empty!", thrown.getMessage());
    }
    @Test
    void createEmptyAmountInvalid() {
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class,
                ()->{
                    AutomaticSaving automaticSaving = new AutomaticSaving("My name", "",
                            "1000000000000062", "1000000000000046");
                });
        assertEquals("Amount cannot be empty!", thrown.getMessage());
    }
    @Test
    void createNegativeAmountInvalid() {
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class,
                ()->{
                    AutomaticSaving automaticSaving = new AutomaticSaving("My name", "-1",
                            "1000000000000062", "1000000000000046");
                });
        assertEquals("Amount must be positive!", thrown.getMessage());
    }
    @Test
    void createNotNumAmountInvalid() {
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class,
                ()->{
                    AutomaticSaving automaticSaving = new AutomaticSaving("My name", "1a",
                            "1000000000000062", "1000000000000046");
                });
        assertEquals("Amount must be a number!", thrown.getMessage());
    }
    @Test
    void createShortSenderInvalid() {
        InvalidAccountNumberException thrown = assertThrows(InvalidAccountNumberException.class,
                ()->{
                    AutomaticSaving automaticSaving = new AutomaticSaving("My name", "1",
                            "100000000000006", "1000000000000046");
                });
        assertEquals("Number must be 16 digits long!", thrown.getMessage());
    }

    @Test
    void createShortRecipientInvalid() {
        InvalidAccountNumberException thrown = assertThrows(InvalidAccountNumberException.class,
                ()->{
                    AutomaticSaving automaticSaving = new AutomaticSaving("My name", "1",
                            "1000000000000062", "100000000000004");
                });
        assertEquals("Number must be 16 digits long!", thrown.getMessage());
    }
    @Test
    void createSameAccInvalid() {
        InvalidAccountNumberException thrown = assertThrows(InvalidAccountNumberException.class,
                ()->{
                    AutomaticSaving automaticSaving = new AutomaticSaving("My name", "1",
                            "1000000000000062", "1000000000000062");
                });
        assertEquals("Accounts must be different!", thrown.getMessage());
    }
}