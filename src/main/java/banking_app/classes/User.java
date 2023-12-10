package banking_app.classes;

import java.util.Scanner;

public class User {
    private static int currId = 0;
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

    public static void register() {
        EmailValidator emailValidator = new EmailValidator();
        PasswordValidator passwordValidator = new PasswordValidator();
        System.out.println("Welcome to our bank!");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your surname: ");
        String surname = scanner.nextLine();
        System.out.print("Enter your email address: ");
        String email;
        while (!emailValidator.validate(email = scanner.nextLine()))
            System.out.print("Enter valid email address: ");
        System.out.print("Enter your password: ");
        String password;
        while (!passwordValidator.validate(password = scanner.nextLine()))
            System.out.print("Enter password that is at least 8 chars long, an uppercase letter, and special character: ");
        System.out.print("Repeat your password: ");
        while (!scanner.nextLine().equals(password))
            System.out.print("Repeat your password: ");
        System.out.println(name + " " + surname + " " + email + " " + password);
    }

    public static void main(String[] args) {
        register();
    }
}

