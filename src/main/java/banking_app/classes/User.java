package banking_app.classes;

import banking_exceptions.*;
import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class User {
    private final int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String reset_code;
    private String pin;
    private List<Account> accounts;
    private static final Scanner scanner = new Scanner(System.in);

    public User(int id, String name, String surname, String email, String password, String reset_code, String pin) {
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

    public void loadAccounts(ConnectionManager manager) throws SQLException {
        this.accounts = manager.findUsersAccounts(this.id);
    }

    public static User register(ConnectionManager manager, String email,
                                String name, String surname, char[] password, char[] repPassword, String pin, String repPin) throws SQLException,
            OccupiedEmailException, InvalidEmailException, InvalidPasswordException, PasswordMissmatchException, InvalidNameException, InvalidPinException, DataMissmatchException {
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
            throw new InvalidEmailException("E-mail cannot  be empty!");
        if (manager.findUser(email) != null)
            throw new OccupiedEmailException("Email already in use!");
        if (!emailValidator.validate(email))
            throw new InvalidEmailException("Wrong email format!");
        if (!passwordValidator.validate(String.valueOf(password)))
            throw new InvalidPasswordException("Password must be 8-20 characters, uppercase letter, lowercase letter," +
                    " number and special character!");
        if (!Arrays.equals(repPassword, password))
            throw new PasswordMissmatchException("Passwords do not match!");
        if (!new PinValidator().validate(pin))
            throw new InvalidPinException("Invalid pin!");
        if (!pin.equals(repPin))
            throw new DataMissmatchException("Pins do not match!");
        if (name.contains(" ") || surname.contains(" "))
            throw new InvalidNameException("Name or surname contains space!");
        manager.registerUser(name, surname, email, String.valueOf(password), pin);
        return manager.findUser(email);
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

    public void makeTransaction(ConnectionManager manager, Transaction transaction) throws SQLException, InvalidAccountNumberException, InvalidAmountException {
        if (manager.findAccount(transaction.getTargetId()) == null)
            throw new InvalidAccountNumberException("Account not existing!");
        Account senderAccount =  manager.findAccount(transaction.getSourceId());
        if (!amountIsInRange(BigDecimal.ZERO, senderAccount.getTransactionLimit(), transaction.getAmount()))
            throw new InvalidAmountException("Transaction exceeds the limit!");
        if (!amountIsInRange(BigDecimal.ZERO, senderAccount.getBalance(), transaction.getAmount()))
            throw new InvalidAmountException("Insufficient funds!");

        manager.registerTransaction(transaction);
        manager.addBalance(transaction.getSourceId(), transaction.getAmount().negate());
        manager.addBalance(transaction.getTargetId(), transaction.getAmount());
    }

    public void createAccount(ConnectionManager manager, String accountName, String transferLimit) throws SQLException, InvalidNameException, InvalidAmountException {
        if (accountName.isEmpty())
            throw new InvalidNameException("Name cannot be empty!");
        if (manager.findAccount(id, accountName) != null)
            throw new InvalidNameException("Name is occupied!");
        if (transferLimit.isEmpty())
            throw new InvalidAmountException("Transfer limit cannot be empty!");
        if (!isBigDecimal(transferLimit))
            throw new InvalidAmountException("Transfer limit must be a number!");
        if (new BigDecimal(transferLimit).compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Transfer limit must be positive!");
        Account account = new Account(id, accountName, new BigDecimal(transferLimit));
        manager.createAccount(account);
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

    public static void createAccountGivenId(ConnectionManager manager, int userId, String accountName, String transferLimit) throws SQLException, InvalidNameException, InvalidAmountException {
        if (accountName.isEmpty())
            throw new InvalidNameException("Name cannot be empty!");
        if (manager.findAccount(userId, accountName) != null)
            throw new InvalidNameException("Name is occupied!");
        if (transferLimit.isEmpty())
            throw new InvalidAmountException("Transfer limit cannot be empty!");
        if (!isBigDecimal(transferLimit))
            throw new InvalidAmountException("Transfer limit must be a number!");
        if (new BigDecimal(transferLimit).compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Transfer limit must be positive!");
        Account account = new Account(userId, accountName, new BigDecimal(transferLimit));
        manager.createAccount(account);
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
        if (newSurname.equals(name))
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
            MissingInformationException, InvalidPasswordException, PasswordMissmatchException, SQLException, DataMissmatchException, RepeatedDataException {
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
        System.out.println("Entered user function!");
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

