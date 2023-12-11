package banking_app.classes;

import connections.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private final int id;
    private String name;
    private String surname;
    private String email;
    private String password;
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
            System.out.println("Niepoprawne dane, spróbój ponownie");
            System.out.print("Wprowadź e-mail: ");
            email = scanner.nextLine();
            System.out.print("Wprowadź hasło: ");
            password = scanner.nextLine();
        }
        return user;
    }
}

