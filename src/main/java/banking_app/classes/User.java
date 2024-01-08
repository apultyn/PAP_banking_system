package banking_app.classes;

import banking_exceptions.*;
import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class User {
    private final int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private List<Account> accounts;
    private static final Scanner scanner = new Scanner(System.in);

    public User(int id, String name, String surname, String email, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }


    public User(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("email"),
                resultSet.getString("password"));
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

    public List<Account> getAccounts() {
        return accounts;
    }

    public void loadAccounts(ConnectionManager manager) throws SQLException {
        this.accounts = manager.findUsersAccounts(this.id);
    }

    public static User register(ConnectionManager manager, String email,
                                String name, String surname, char[] password, char[] repPassword) throws SQLException,
            OccupiedEmailException, InvalidEmailException, InvalidPasswordException, PasswordMissmatchException, InvalidNameException {
        EmailValidator emailValidator = new EmailValidator();
        PasswordValidator passwordValidator = new PasswordValidator();

        if (manager.findUser(email) != null)
            throw new OccupiedEmailException("Email already in use!");
        if (!emailValidator.validate(email))
            throw new InvalidEmailException("Wrong email format!");
        if (!passwordValidator.validate(String.valueOf(password)))
            throw new InvalidPasswordException("Wrong password format!");
        if (!Arrays.equals(repPassword, password))
            throw new PasswordMissmatchException("Password not repeated correctly!");
        if (name.contains(" ") || surname.contains(" "))
            throw new InvalidNameException("Name or surname contains space!");
        manager.registerUser(name, surname, email, String.valueOf(password));
        return manager.findUser(email);
    }

    public static User login(ConnectionManager manager,
                             String email, char[] password) throws SQLException, LoginFailedException {
        User user;
        user = manager.findUser(email);
        if (user == null || !String.valueOf(password).equals(user.getPassword()))
            throw new LoginFailedException("Incorrect logging data");
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
    public void makeTransaction(ConnectionManager manager, String recipientName, String recipientAccountNumber,
                                String senderAccountNumber, String title, String amount) throws SQLException, InvalidAccountNumberException, InvalidNameException, InvalidAmountException {
        if (recipientName.isEmpty()) {
            throw new InvalidNameException("Recipient name cannot be empty!");
        }
        if (!recipientAccountNumber.matches("\\d{16}")) {
            throw new InvalidAccountNumberException("Number must be 16 digits long!");
        }
        if (manager.findAccount(Long.parseLong(recipientAccountNumber)) == null) {
            throw new InvalidAccountNumberException("Account not existing!");
        }
        if (manager.findUsersAccounts(this.id).contains(manager.findAccount(Long.parseLong(recipientAccountNumber)))) {
            throw new InvalidAccountNumberException("Account cannot be yours!");
        }
        if (title.isEmpty()) {
            throw new InvalidNameException("Title cannot be empty!");
        }
        if (amount.isEmpty()) {
            throw new InvalidAmountException("Amount cannot be empty!");
        }
        if (!isBigDecimal(amount)) {
            throw new InvalidAmountException("Amount must be a number!");
        }
        Account senderAccount =  manager.findAccount(Long.parseLong(senderAccountNumber));
        Account recipientAccount = manager.findAccount(Long.parseLong(recipientAccountNumber));
        if (new BigDecimal(amount).compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive!");
        }
        if (!amountIsInRange(BigDecimal.ZERO, BigDecimal.valueOf(senderAccount.getTransactionLimit()), new BigDecimal(amount))) {
            throw new InvalidAmountException("Transaction exceeds the limit!");
        }
        if (!amountIsInRange(BigDecimal.ZERO, senderAccount.getBalance(), new BigDecimal(amount))) {
            throw new InvalidAmountException("Insufficient funds!");
        }

        Transaction transaction = new Transaction(recipientAccount.getAccountId(), senderAccount.getAccountId(), title, new BigDecimal(amount), 1);
        manager.registerTransaction(transaction);
        manager.addBalance(transaction.getSourceId(), transaction.getAmount().negate());
        manager.addBalance(transaction.getTargetId(), transaction.getAmount());
    }

    public void createAccount(ConnectionManager manager) throws SQLException {
        while (true) {
            try {
                System.out.print("Wprowadź nazwę nowego konta: ");
                String name = scanner.nextLine();
                BigDecimal limit;
                System.out.print("Wpisz limit pojedynczej transakcji: ");
                String limitAns = scanner.nextLine();
                if (!limitAns.isBlank()) {
                    limit = new BigDecimal(limitAns);
                    manager.createAccount(name, limit, this.getId());
                    break;
                } else {
                    throw new NumberFormatException("");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Już masz konto o tej nazwie");
            } catch (NumberFormatException e) {
                System.out.println("Podaj prawidłową liczbę");
            }
        }
    }
    public void updateFirstName(ConnectionManager manager, String  newName) {
        manager.updateUserFirstName(id, newName);
    }

    public void updateSurname(ConnectionManager manager, String newSurname) {
        manager.updateUserSurname(id, newSurname);
    }

    public void updateEmail(ConnectionManager manager, String newEmail) {
        manager.updateUserEmail(id, newEmail);
    }

    public void updatePassword(ConnectionManager manager, String oldPassword, String newPassword, String repNewPassword) {
        manager.updateUserPassword(manager, password);
    }
}

