package banking_app.classes;

import connections.ConnectionManager;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
        manager.findUsersAccounts(this.id);
    }

    public static User register(ConnectionManager manager) throws SQLException {
        EmailValidator emailValidator = new EmailValidator();
        PasswordValidator passwordValidator = new PasswordValidator();

        String email;
        System.out.print("Wprowadź e-mail: ");
        email = scanner.nextLine();
        while (!emailValidator.validate(email) ||
                manager.findUser(email) != null) {
            if (!emailValidator.validate(email)) {
                System.out.print("Niepoprawny e-mail, wprowadź ponownie: ");
                email = scanner.nextLine();

            } else {
                System.out.print("E-mail jest już wykorzystany, wprowadź ponownie: ");
                email = scanner.nextLine();
            }
        }

        String password;
        System.out.print("Wprowadź hasło (8-20 znaków, min. 1: cyfra, mała litera, duża litera, znak zpecjalny (!@#$%&*()-+=^), bez spacji): ");
        while (!passwordValidator.validate(password = scanner.nextLine())) {
            System.out.print("Niepoprawne hasło, wprowadź ponownie: ");
        }
        System.out.print("Powtórz hasło: ");
        while (!scanner.nextLine().equals(password)) {
            System.out.print("Hasła nie pasują, wprowadź ponownie: ");
        }

        String name;
        System.out.print("Wprowadź imię: ");
        while ((name = scanner.nextLine()).contains(" ")) {
            System.out.print("Imię nie może mieć spacji, wprowadź ponownie: ");
        }

        String surname;
        System.out.print("Wprowadź nazwisko: ");
        while ((surname = scanner.nextLine()).contains(" ")) {
            System.out.print("Nazwisko nie może mieć spacji, wprowadź ponownie: ");
        }
        manager.registerUser(name, surname, email, password);
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

    public static boolean amountIsInRange(float a, float b, float x) {
        return (x > a && x <= b);
    }
    public static boolean isFloat(String num)
    {
        try{
            Float.parseFloat(num);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    public void makeTransaction(ConnectionManager manager) throws SQLException {
        System.out.print("Na rachunek: ");
        String input;
        while (!(input = scanner.nextLine()).matches("\\d{16}")) {
            System.out.print("Numer rachunku musi się składać z 16 cyfr, wprowadź ponownie: ");
        }
        long targetAccountId = Long.parseLong(input);

        System.out.print("Z rachunku: ");
        while (!(input = scanner.nextLine()).matches("\\d{16}")) {
            System.out.print("Numer rachunku musi się składać z 16 cyfr, wprowadź ponownie: ");
        }
        long sourceAccountId = Long.parseLong(input);
        Account sourceAccount = manager.findAccount(sourceAccountId);
        System.out.print("Kwota przelewu: ");
        float amount;
        input = scanner.nextLine();
        while (!isFloat(input) || !amountIsInRange(0, sourceAccount.getTransactionLimit(), Float.parseFloat(input))
            || sourceAccount.getBalance() < Float.parseFloat(input)) {
            System.out.print("Niepoprawna kwota, wprowadź ponownie: ");
            input = scanner.nextLine();
        }
        amount = Float.parseFloat(input);
        System.out.print("Tytuł przelewu: ");
        String title = scanner.nextLine();
        manager.registerTransaction(title, amount, 1, sourceAccountId, targetAccountId);
        manager.addBalance(sourceAccountId, -amount);
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

