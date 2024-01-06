package banking_app.classes;

import banking_exceptions.*;
import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
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
                                String name, String surname, char[] password, char[] repPassword) throws SQLException, Exception {
        EmailValidator emailValidator = new EmailValidator();
        PasswordValidator passwordValidator = new PasswordValidator();

        if (manager.findUser(email) != null)
            throw new OccupiedEmailException("Email already in use!");
        if (!emailValidator.validate(email))
            throw new InvalidEmailException("Wrong email format!");
        if (!passwordValidator.validate(String.valueOf(password)))
            throw new InvalidPasswordException("Wrong password format!");
        if (!repPassword.equals(password))
            throw new PasswordMissmatchException("Password not repeated correctly!");
        if (name.contains(" ") || surname.contains(" "))
            throw new InvalidNameException("Name or surname contains space!");
        manager.registerUser(name, surname, email, String.valueOf(password));
        return manager.findUser(email);
    }

    public static User login(ConnectionManager manager) throws SQLException {
        String email;
        String password;
        User user;
        System.out.print("Wprowadź e-mail: ");
        email = scanner.nextLine();
        System.out.print("Wprowadź hasło: ");
        password = scanner.nextLine();
        while ((user = manager.findUser(email)) == null || !password.equals(user.getPassword())) {
            System.out.println("Niepoprawne dane, wprowadź ponownie");
            System.out.print("Wprowadź e-mail: ");
            email = scanner.nextLine();
            System.out.print("Wprowadź hasło: ");
            password = scanner.nextLine();
        }
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
    public void makeTransaction(ConnectionManager manager) throws SQLException {
        ArrayList<Long> usersAccountsIds = new ArrayList<>();
        manager.findUsersAccounts(this.getId()).forEach(account -> usersAccountsIds.add(account.getAccountId()));
        System.out.print("Na rachunek: ");
        String input = scanner.nextLine();
        long targetAccountId;
        while (!(input.matches("\\d{16}"))) {
            System.out.print("Numer rachunku musi się składać z 16 cyfr, wprowadź ponownie: ");
            input = scanner.nextLine();
        }
        targetAccountId = Long.parseLong(input);

        long sourceAccountId;
        System.out.print("Z rachunku: ");
        input = scanner.nextLine();
        while (!input.matches("\\d{16}" ) || !usersAccountsIds.contains(sourceAccountId = Long.parseLong(input))) {
            if (!input.matches("\\d{16}" ))
                System.out.print("Numer rachunku musi się składać z 16 cyfr, wprowadź ponownie: ");
            else
                System.out.print("Rachunek musi należeć do ciebie, wprowadź ponownie: ");
            input = scanner.nextLine();
        }
        Account sourceAccount = manager.findAccount(sourceAccountId);
        System.out.print("Kwota przelewu: ");
        BigDecimal amount;
        input = scanner.nextLine();
        while (!isBigDecimal(input) ||
                !amountIsInRange(BigDecimal.ZERO, BigDecimal.valueOf(sourceAccount.getTransactionLimit()).min(BigDecimal.valueOf(sourceAccount.getBalance())), new BigDecimal(input))) {
            System.out.print("Niepoprawna kwota, wprowadź ponownie: ");
            input = scanner.nextLine();
        }
        amount = new BigDecimal(input);
        System.out.print("Tytuł przelewu: ");
        String title = scanner.nextLine();
        manager.registerTransaction(title, amount, 1, sourceAccountId, targetAccountId);
        manager.addBalance(sourceAccountId, amount.negate());
        manager.addBalance(targetAccountId, amount);
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
}

