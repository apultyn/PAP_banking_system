package banking_app.classes;

import banking_exceptions.*;
import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class User {
    private final Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private final String reset_code;
    private String pin;
    private List<Account> accounts;

    public User(Integer id, String name, String surname, String email, String password, String reset_code, String pin) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.reset_code = reset_code;
        this.pin = pin;
    }

    public User(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("reset_code"),
                resultSet.getString("pin"));
    }

    public User(String email, String name, String surname, char[] password, String pin) throws
            InvalidNameException, InvalidEmailException, InvalidPasswordException, InvalidPinException {
        EmailValidator emailValidator = new EmailValidator();
        PasswordValidator passwordValidator = new PasswordValidator();
        if (name.isEmpty())
            throw new InvalidNameException("Name cannot be empty!");
        if (name.contains(" "))
            throw new InvalidNameException("Name cannot contain space!");
        if (surname.isEmpty())
            throw new InvalidNameException("Last name cannot be empty!");
        if (surname.contains(" "))
            throw new InvalidNameException("Last name cannot contain space!");
        if (email.isEmpty())
            throw new InvalidEmailException("E-mail cannot be empty!");
        if (!emailValidator.validate(email))
            throw new InvalidEmailException("Wrong email format!");
        if (!passwordValidator.validate(String.valueOf(password)))
            throw new InvalidPasswordException("Password must be 8-20 characters, uppercase letter, lowercase letter," +
                    " number and special character!");
        if (!new PinValidator().validate(pin))
            throw new InvalidPinException("Invalid pin!");
        if (name.contains(" ") || surname.contains(" "))
            throw new InvalidNameException("Name or surname contains space!");
        this.id = null;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = String.valueOf(password);
        this.reset_code = null;
        this.pin = pin;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getReset_code() {
        return reset_code;
    }

    public String getPin() {
        return pin;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public static void register(ConnectionManager manager, User user, Account account) throws SQLException {
        manager.registerUser(user);
        account.setUserId(manager.findUser(user.getEmail()).getId());
        manager.createAccount(account);
    }

    public static User createNewUser(ConnectionManager manager, String email, String name, String surname,
                                     char[] password, char[] repPassword, String pin, String repPin) throws
            SQLException, OccupiedEmailException, PasswordMissmatchException, DataMissmatchException, InvalidNameException,
            InvalidPasswordException, InvalidEmailException, InvalidPinException {
        User user = new User(email, name, surname, password, pin);
        if (manager.findUser(user.getEmail()) != null)
            throw new OccupiedEmailException("Email already in use!");
        if (!Arrays.equals(repPassword, user.getPassword().toCharArray()))
            throw new PasswordMissmatchException("Passwords do not match!");
        if (!user.getPin().equals(repPin))
            throw new DataMissmatchException("Pins do not match!");
        return user;
    }

    public static User login(ConnectionManager manager,
                             String email, char[] password) throws SQLException, LoginFailedException {
        User user;
        user = manager.findUser(email);
        if (user == null || !String.valueOf(password).equals(user.getPassword()))
            throw new LoginFailedException("Incorrect e-mail or password!");
        return user;
    }

    public static boolean amountIsInRange(BigDecimal a, BigDecimal b, BigDecimal x) {
        return (x.compareTo(a) > 0 && x.compareTo(b) <= 0);
    }

    public static boolean isBigDecimal(String num)
    {
        try {
            new BigDecimal(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void makeTransfer(ConnectionManager manager, Transfer transfer) throws
            SQLException, InvalidAccountNumberException, InvalidAmountException {
        if (manager.findAccount(transfer.getTargetId()) == null)
            throw new InvalidAccountNumberException("Account not existing!");
        Account senderAccount =  manager.findAccount(transfer.getSourceId());
        if (!amountIsInRange(BigDecimal.ZERO, senderAccount.getTransferLimit(), transfer.getAmount()))
            throw new InvalidAmountException("Transfer exceeds the limit!");
        if (!amountIsInRange(BigDecimal.ZERO, senderAccount.getBalance(), transfer.getAmount()))
            throw new InvalidAmountException("Insufficient funds!");

        manager.registerTransfer(transfer);
        manager.addBalance(transfer.getSourceId(), transfer.getAmount().negate());
        manager.addBalance(transfer.getTargetId(), transfer.getAmount());
    }

    public static Account createAccount(ConnectionManager manager, User user, String accountName, String transferLimit) throws SQLException, InvalidNameException, InvalidAmountException {
        if (user.id != null) {
            if (manager.findAccount(user.id, accountName) != null)
                throw new InvalidNameException("Name is occupied!");
            return new Account(accountName, transferLimit, user.id);
        }
        else
            return new Account(accountName, transferLimit, null);
    }

    public static void checkInputForFirst(String accountName, String transferLimit) throws InvalidNameException, InvalidAmountException {
        if (accountName.isEmpty())
            throw new InvalidNameException("Name cannot be empty!");
        if (transferLimit.isEmpty())
            throw new InvalidAmountException("Transfer limit cannot be empty!");
        if (!isBigDecimal(transferLimit))
            throw new InvalidAmountException("Transfer limit must be a number!");
        if (new BigDecimal(transferLimit).compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Transfer limit must be positive!");
    }

    public void updateFirstName(ConnectionManager manager, String oldName, String newName) throws
            InvalidNameException, SQLException, MissingInformationException, RepeatedDataException, DataMissmatchException {
        if (oldName.isEmpty() || newName.isEmpty())
            throw new MissingInformationException("Missing data!");
        if (!oldName.equals(name))
            throw new DataMissmatchException("Wrong old name!");
        if (newName.equals(name))
            throw new RepeatedDataException("New name can't be the same as previous!");
        if (newName.contains(" "))
            throw new InvalidNameException("New name contains space!");
        manager.updateUserFirstName(id, newName);
        name = newName;
    }

    public void updateSurname(ConnectionManager manager, String oldSurname, String newSurname) throws
            InvalidNameException, SQLException, MissingInformationException, RepeatedDataException, DataMissmatchException {
        if (oldSurname.isEmpty() || newSurname.isEmpty())
            throw new MissingInformationException("Missing data!");
        if (!oldSurname.equals(surname))
            throw new DataMissmatchException("Wrong old surname!");
        if (newSurname.equals(surname))
            throw new RepeatedDataException("New surname can't be the same as previous!");
        if (newSurname.contains(" "))
            throw new InvalidNameException("New surname contains space!");
        manager.updateUserSurname(id, newSurname);
        surname = newSurname;
    }

    public void updateEmail(ConnectionManager manager, String oldEmail, String newEmail) throws
            MissingInformationException, RepeatedDataException, InvalidEmailException, SQLException, DataMissmatchException {
        if (oldEmail.isEmpty() || newEmail.isEmpty())
            throw new MissingInformationException("New email can't be null!");
        if (!oldEmail.equals(email))
            throw new DataMissmatchException("Wrong old email!");
        if (newEmail.equals(email))
            throw new RepeatedDataException("New email can't be the same as previous!");
        if (!new EmailValidator().validate(newEmail))
            throw new InvalidEmailException("Email in wrong format!");
        manager.updateUserEmail(id, newEmail);
        email = newEmail;
    }

    public void updatePassword(ConnectionManager manager, String oldPassword, String newPassword, String repNewPassword) throws
            MissingInformationException, InvalidPasswordException, PasswordMissmatchException, SQLException, DataMissmatchException,
            RepeatedDataException {
        if (oldPassword.isEmpty() || newPassword.isEmpty() || repNewPassword.isEmpty())
            throw new MissingInformationException("Missing some passwords!");
        if (!oldPassword.equals(password))
            throw new DataMissmatchException("Wrong old password!");
        if (password.equals(newPassword))
            throw new RepeatedDataException("New password can't be the same as old!");
        if (!new PasswordValidator().validate(newPassword))
            throw new InvalidPasswordException("Wrong password format!");
        if (!newPassword.equals(repNewPassword))
            throw new PasswordMissmatchException("New password not repeated correctly!");
        manager.updateUserPassword(id, newPassword);
        password = newPassword;
    }

    public void updatePin(ConnectionManager manager, String oldPin, String newPin, String repPin) throws
            MissingInformationException, InvalidPinException, RepeatedDataException, DataMissmatchException,
            SQLException, PinMissmatchException {
        if (oldPin.isEmpty() || newPin.isEmpty() || repPin.isEmpty())
            throw new MissingInformationException("Fields can't be empty");
        if (!oldPin.equals(pin))
            throw new DataMissmatchException("Wrong old pin!");
        if (pin.equals(newPin))
            throw new RepeatedDataException("New pin can't be the same as previous!");
        if (!new PinValidator().validate(newPin))
            throw new InvalidPinException("Pin invalid!");
        if (!newPin.equals(repPin))
            throw new PinMissmatchException("Pin not repeated correctly!");
        manager.updateUserPin(id, newPin);
        pin = newPin;
    }

    public void createContact(ConnectionManager manager, String name, String accountId) throws
            SQLException, MissingInformationException, AccountNotFoundException, InvalidAccountNumberException {
        if(name.isEmpty() || accountId.isEmpty()) {
            throw new MissingInformationException("Fields can not be empty.");
        }
        if (!isBigDecimal(accountId))
            throw new InvalidAccountNumberException("Account Number must be a number!");
        if (manager.findAccount(Long.parseLong(accountId)) == null) {
            throw new AccountNotFoundException("Account does not exists!");
        }
        List<Account> usersAccounts = manager.findUsersAccounts(this.id);
        for (Account account : usersAccounts) {
            if (account.getAccountId() == Long.parseLong(accountId)){
                throw new InvalidAccountNumberException("Can not add your own account!");
            }
        }
        manager.createContact(name, Long.parseLong(accountId), this.id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}

