package banking_app.gui;

import banking_app.classes.User;
import banking_exceptions.*;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegisterPanel extends JPanel {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField passwordRepeatedField;
    private JButton registerButton;
    private JButton backButton;
    private ConnectionManager manager;

    public RegisterPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        this.manager = manager;

        // Ustawienia dla komponentów
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        backButton = new JButton("cofnij");
        backButton.addActionListener(e-> cardLayout.show(cardPanel, "Login"));
        add(backButton);

        add(new JLabel("Rejestracja"), gbc);

        add(new JLabel("Imię"), gbc);

        firstNameField = new JTextField(10);
        add(firstNameField);

        add(new JLabel("Nazwisko"), gbc);

        lastNameField = new JTextField(10);
        add(lastNameField);

        add(new JLabel("Email"), gbc);

        emailField = new JTextField(20);
        add(emailField);

        add(new JLabel("Podaj haslo"), gbc);

        passwordField = new JPasswordField(20);
        add(passwordField);


        add(new JLabel("Powtorz haslo"), gbc);

        passwordRepeatedField = new JPasswordField(20);
        add(passwordRepeatedField);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister(cardLayout, cardPanel));
        add(registerButton);
    }

    private void handleRegister(CardLayout cardLayout, JPanel cardPanel) {
        String email = emailField.getText();
        String name = firstNameField.getText();
        String surname = lastNameField.getText();
        char[] password = passwordField.getPassword();
        char[] passwordRepeated = passwordRepeatedField.getPassword();
        User user;
        try {
            user = User.register(manager, email, name, surname, password, passwordRepeated);
            JOptionPane.showMessageDialog(this, "Zarejestrowano");
            cardLayout.show(cardPanel, "Login");
        } catch (OccupiedEmailException a) {
            JOptionPane.showMessageDialog(this, "Email zostal uzyty");
        } catch (InvalidEmailException a) {
            JOptionPane.showMessageDialog(this, "Niepoprawny email");
        } catch (InvalidPasswordException a) {
            JOptionPane.showMessageDialog(this, "Niepoprawne haslo");
        } catch (PasswordMissmatchException a) {
            JOptionPane.showMessageDialog(this, "Hasla nie sa takie same");
        } catch (InvalidNameException a) {
            JOptionPane.showMessageDialog(this, "Zle dane w polu imie");
        } catch (SQLException a) {
            JOptionPane.showMessageDialog(this, "Blad bazy");
        }
    }


    // Metody dostępu do pól formularza
    public String getFirstName() {
        return firstNameField.getText();
    }

    public String getLastName() {
        return lastNameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }
}
