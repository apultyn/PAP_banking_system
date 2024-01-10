package banking_app.classes;

import banking_exceptions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    void getId() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getId() == 2;
    }

    @Test
    void getName() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getName().equals("Jan");
    }

    @Test
    void getSurname() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getSurname().equals("Kowalski");
    }

    @Test
    void getEmail() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getEmail().equals("example@gm.pl");
    }

    @Test
    void getPassword() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getPassword().equals("Password123!");
    }

    @Test
    void getReset_code() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getReset_code().equals("1234");
    }

    @Test
    void getPin() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getPin().equals("9999");
    }

    @Test
    void getAccountsIsNullFirst() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        List<Account> accounts = user.getAccounts();
        assert accounts == null;
    }


    @Test
    void register() {
//        if (name.isEmpty())
//            throw new InvalidNameException("Name cannot be empty!");
//        if (name.contains(" "))
//            throw new InvalidNameException("Name cannot contain space!");
//        if (surname.isEmpty())
//            throw new InvalidNameException("Last name cannot be empty!");
//        if (surname.contains(" "))
//            throw new InvalidNameException("Last name cannot contain space!");
//        if (email.isEmpty())
//            throw new InvalidEmailException("E-mail cannot  be empty!");
//        if (manager.findUser(email) != null)
//            throw new OccupiedEmailException("Email already in use!");
//        if (!emailValidator.validate(email))
//            throw new InvalidEmailException("Wrong email format!");
//        if (!passwordValidator.validate(String.valueOf(password)))
//            throw new InvalidPasswordException("Password must be 8-20 characters, uppercase letter, lowercase letter," +
//                    " number and special character!");
//        if (!Arrays.equals(repPassword, password))
//            throw new PasswordMissmatchException("Passwords do not match!");
//        if (!new PinValidator().validate(pin))
//            throw new InvalidPinException("Invalid pin!");
//        if (!pin.equals(repPin))
//            throw new DataMissmatchException("Pins do not match!");
//        if (name.contains(" ") || surname.contains(" "))
//            throw new InvalidNameException("Name or surname contains space!");
//        InvalidNameException thrown = assertThrows(InvalidNameException,
//                () -> {
//                    User.register(null, "email@op.pl", "Name",
//                            "Surname", String.valueOf("Password123!"), "Password123!", "1234", "1234");
//                });
    }


    @Test
    void amountIsInRange() {
        assert User.amountIsInRange(new BigDecimal("1000000"), new BigDecimal("200000000"), new BigDecimal("5000000")) ;
    }

    @Test
    void amountIsNotInRange() {
        assert !User.amountIsInRange(new BigDecimal("1000000"), new BigDecimal("200000000"), new BigDecimal("50")) ;
    }

    @Test
    void isBigDecimalTest() {
        assert User.isBigDecimal("77238238");
    }

    @Test
    void isBigDecimalTestLetter() {
        assert !User.isBigDecimal("abc");
    }

    @Test
    void isBigDecimalFloat() {
        assert User.isBigDecimal("77238238.23");
    }

//    @Test
//    void makeTransaction() {
//
//    }

//    @Test
//    void createAccountEmptyName() {
//        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
//        InvalidNameException thrown = assertThrows(InvalidNameException.class, ()-> {
//            user.createAccount(null, user,"", "100");
//        });
//        assertEquals("Name cannot be empty!", thrown.getMessage());
//    }

//    @Test
//    void createAccountEmptyLimit() {
//        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
//        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, ()-> {
//            user.createAccount(null, user, "Main acc", "");
//        });
//        assertEquals("Transfer limit cannot be empty!", thrown.getMessage());
//
//    }

//    @Test
//    void createAccountLimitWrongFormat() {
//        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
//        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, ()-> {
//            user.createAccount(null, user, "Main acc", "200a");
//        });
//        assertEquals("Transfer limit must be a number!", thrown.getMessage());
//
//    }

//    @Test
//    void createAccountLimitNegative() {
//        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
//        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, ()-> {
//            user.createAccount(null, user, "Main acc", "-200.02");
//        });
//        assertEquals("Transfer limit must be positive!", thrown.getMessage());
//
//    }

    @Test
    void checkInputForFirstInvalidNameException() {
        InvalidNameException thrown = assertThrows(InvalidNameException.class,
                () -> User.checkInputForFirst("", "200"));
        assertEquals("Name cannot be empty!", thrown.getMessage());

    }

    @Test
    void checkInputForFirstInvalidAmountException() {
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class,
                () -> User.checkInputForFirst("Main account", ""));
        assertEquals("Transfer limit cannot be empty!", thrown.getMessage());

    }

    @Test
    void checkInputForFirstInvalidAmountException2() {
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class,
                () -> User.checkInputForFirst("Main account", "abc"));
        assertEquals("Transfer limit must be a number!", thrown.getMessage());
    }

    @Test
    void checkInputForFirstInvalidAmountExceptionPositive() {
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class,
                () -> User.checkInputForFirst("Main account", "-2637.23"));
        assertEquals("Transfer limit must be positive!", thrown.getMessage());
    }


    @Test
    void createAccountGivenId() throws SQLException, InvalidNameException, InvalidAmountException {
        //User.createAccountGivenId(new ConnectionManager(), 2, "Main", "200");
    }

    @Test
    void updateFirstNameEmpty() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        MissingInformationException thrown = assertThrows(MissingInformationException.class,
                () -> {
                    user.updateFirstName(null, "", "NewName");
                });
        assertEquals("Missing data!", thrown.getMessage());
    }

    @Test
    void updateFirstNameWrongOldName() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        DataMissmatchException thrown = assertThrows(DataMissmatchException.class,
                () -> {
                    user.updateFirstName(null, "Name2", "NewName");
                });
        assertEquals("Wrong old name!", thrown.getMessage());
    }

    @Test
    void updateFirstNameWrongNewName() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        RepeatedDataException thrown = assertThrows(RepeatedDataException.class,
                () -> {
                    user.updateFirstName(null, "Name", "Name");
                });
        assertEquals("New name can't be the same as previous!", thrown.getMessage());
    }

    @Test
    void updateFirstNameNameHasSpace() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        InvalidNameException thrown = assertThrows(InvalidNameException.class,
                () -> {
                    user.updateFirstName(null, "Name", "New Name");
                });
        assertEquals("New name contains space!", thrown.getMessage());
    }

    @Test
    void updateSurnameEmpty() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        MissingInformationException thrown = assertThrows(MissingInformationException.class,
                () -> {
                    user.updateFirstName(null, "", "NewName");
                });
        assertEquals("Missing data!", thrown.getMessage());
    }

    @Test
    void updateSurnameWrongOldSurname() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        DataMissmatchException thrown = assertThrows(DataMissmatchException.class,
                () -> {
                    user.updateSurname(null, "Surname2", "NewSurname");
                });
        assertEquals("Wrong old surname!", thrown.getMessage());
    }

    @Test
    void updateSurnameWrongNewSurname() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        RepeatedDataException thrown = assertThrows(RepeatedDataException.class,
                () -> {
                    user.updateSurname(null, "Surname", "Surname");
                });
        assertEquals("New surname can't be the same as previous!", thrown.getMessage());
    }

    @Test
    void updateSurnameHasSpace() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        InvalidNameException thrown = assertThrows(InvalidNameException.class,
                () -> {
                    user.updateSurname(null, "Surname", "New Surname");
                });
        assertEquals("New surname contains space!", thrown.getMessage());
    }

    @Test
    void updateEmailEmpty() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        MissingInformationException thrown = assertThrows(MissingInformationException.class,
                ()->{
                    user.updateEmail(null, "", "email@gm.pl");
                });
        assertEquals("New email can't be null!", thrown.getMessage());
    }

    @Test
    void updateEmailWrongOldEmail() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        DataMissmatchException thrown = assertThrows(DataMissmatchException.class,
                ()->{
                    user.updateEmail(null, "email@gm.pl", "email@gm.pll");
                });
        assertEquals("Wrong old email!", thrown.getMessage());
    }

    @Test
    void updateEmailWrongNewEmail() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        RepeatedDataException thrown = assertThrows(RepeatedDataException.class,
                ()->{
                    user.updateEmail(null, "email@op.pl", "email@op.pl");
                });
        assertEquals("New email can't be the same as previous!", thrown.getMessage());
    }

    @Test
    void updateEmailInvalid() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        InvalidEmailException thrown = assertThrows(InvalidEmailException.class,
                ()->{
                    user.updateEmail(null, "email@op.pl", "emailgp.pl");
                });
        assertEquals("Email in wrong format!", thrown.getMessage());
    }


    @Test
    void updatePasswordEmpty() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        MissingInformationException thrown = assertThrows(MissingInformationException.class,
                ()->{
                    user.updatePassword(null, "Password123!", "Password1#", "");
                });
        assertEquals("Missing some passwords!", thrown.getMessage());
    }

    @Test
    void updatePasswordWrongOldPassword() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        DataMissmatchException thrown = assertThrows(DataMissmatchException.class,
                ()->{
                    user.updatePassword(null, "Password2!", "Password1#", "Password1#");
                });
        assertEquals("Wrong old password!", thrown.getMessage());
    }

    @Test
    void updatePasswordWrongNewPassword() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        RepeatedDataException thrown = assertThrows(RepeatedDataException.class,
                ()->{
                    user.updatePassword(null, "Password123!", "Password123!", "Password123!");
                });
        assertEquals("New password can't be the same as old!", thrown.getMessage());
    }

    @Test
    void updatePasswordInvalid() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        InvalidPasswordException thrown = assertThrows(InvalidPasswordException.class,
                ()->{
                    user.updatePassword(null, "Password123!", "Password12", "Password12");
                });
        assertEquals("Wrong password format!", thrown.getMessage());
    }

    @Test
    void updatePasswordMismatch() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        PasswordMissmatchException thrown = assertThrows(PasswordMissmatchException.class,
                ()->{
                    user.updatePassword(null, "Password123!", "Password124#", "Password123#");
                });
        assertEquals("New password not repeated correctly!", thrown.getMessage());
    }

    @Test
    void updatePinEmpty() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        MissingInformationException thrown = assertThrows(MissingInformationException.class,
                ()->{
                    user.updatePin(null, "9999", "", "3434");
                });
        assertEquals("Fields can't be empty", thrown.getMessage());
    }

    @Test
    void updatePinWrongOldPin() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        DataMissmatchException thrown = assertThrows(DataMissmatchException.class,
                ()->{
                    user.updatePin(null, "999", "3434", "3434");
                });
        assertEquals("Wrong old pin!", thrown.getMessage());
    }

    @Test
    void updatePinWrongNewPin() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        RepeatedDataException thrown = assertThrows(RepeatedDataException.class,
                ()->{
                    user.updatePin(null, "9999", "9999", "9999");
                });
        assertEquals("New pin can't be the same as previous!", thrown.getMessage());
    }

    @Test
    void updatePinInvalid() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        InvalidPinException thrown = assertThrows(InvalidPinException.class,
                ()->{
                    user.updatePin(null, "9999", "343", "343");
                });
        assertEquals("Pin invalid!", thrown.getMessage());
    }

    @Test
    void updatePinMismatch() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        PinMissmatchException thrown = assertThrows(PinMissmatchException.class,
                ()->{
                    user.updatePin(null, "9999", "3434", "3435");
                });
        assertEquals("Pin not repeated correctly!", thrown.getMessage());
    }


    @Test
    void createContactEmpty() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        MissingInformationException thrown = assertThrows(MissingInformationException.class,
                ()->{
                    user.createContact(null, "", "100000000002");
                });
        assertEquals("Fields can not be empty.", thrown.getMessage());
    }

    @Test
    void createContactNotNumber() {
        User user = new User(1000, "Name", "Surname", "email@op.pl", "Password123!", "1234", "9999");
        InvalidAccountNumberException thrown = assertThrows(InvalidAccountNumberException.class,
                ()->{
                    user.createContact(null, "Adam", "100000000002a");
                });
        assertEquals("Account Number must be a number!", thrown.getMessage());
    }

}