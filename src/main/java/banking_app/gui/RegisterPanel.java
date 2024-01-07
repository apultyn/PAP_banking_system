package banking_app.gui;

import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Ustawienia dla komponentów
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; // Kolumna 0
        gbc.gridy = 0; // Wiersz 0
        gbc.gridwidth = 2; // Rozciąga etykietę "Rejestracja" na 2 kolumny
        add(new JLabel("Rejestracja"), gbc);

        gbc.gridwidth = 1; // Reset do 1 kolumny dla następnych komponentów
        gbc.gridy++; // Wiersz 1
        gbc.gridx = 0; // Kolumna 0
        add(new JLabel("Imię"), gbc);

        gbc.gridx++; // Kolumna 1
        firstNameField = new JTextField(10);
        add(firstNameField, gbc);

        gbc.gridx++; // Kolumna 2
        add(new JLabel("Nazwisko"), gbc);

        gbc.gridx++; // Kolumna 3
        lastNameField = new JTextField(10);
        add(lastNameField, gbc);

        gbc.gridx = 0; // Reset do kolumny 0 dla nowego wiersza
        gbc.gridy++; // Wiersz 2
        add(new JLabel("Email"), gbc);

        gbc.gridx++; // Kolumna 1
        gbc.gridwidth = 3; // Rozciąga pole email przez 3 kolumny
        emailField = new JTextField(20);
        add(emailField, gbc);


    }

    private void handleRegister() {
        String email = emailField.getText();
        String name = firstNameField.getText();
//        String surname = surnameField.getText();
//        char[] password = passwordField1.getPassword();
//        char[] passwordRepeated = passwordField2.getPassword();
//        User user;
//        try {
//            user = User.register(manager, email, name, surname, password, passwordRepeated);
//        } catch (OccupiedEmailException a) {
//            JOptionPane.showMessageDialog(banking_app.classes.Menu.this, "Email zostal uzyty");
//        } catch (InvalidEmailException a) {
//            JOptionPane.showMessageDialog(banking_app.classes.Menu.this, "Niepoprawny email");
//        } catch (InvalidPasswordException a) {
//            JOptionPane.showMessageDialog(banking_app.classes.Menu.this, "Niepoprawne haslo");
//        } catch (PasswordMissmatchException a) {
//            JOptionPane.showMessageDialog(banking_app.classes.Menu.this, "Hasla nie sa takie same");
//        } catch (InvalidNameException a) {
//            JOptionPane.showMessageDialog(banking_app.classes.Menu.this, "Zle dane w polu imie");
//        } catch (SQLException a) {
//            JOptionPane.showMessageDialog(Menu.this, "Blad bazy");
//        }
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
