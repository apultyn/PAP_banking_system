package banking_app.gui;

import banking_app.classes.User;
import banking_exceptions.LoginFailedException;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public LoginPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        JLabel userLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "Register"));

        add(userLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);
    }

    public void handleLogin() {
        String email = emailField.getText();
        char[] password = passwordField.getPassword();
        User user;
        try {
            user = User.login(manager, email, password);
            System.out.println("Zalogowano");
            JOptionPane.showMessageDialog(this, "Zalogowano");
            UserProfilePanel userPanel = (UserProfilePanel) cardPanel.getComponent(2);
            userPanel.setUser(user);
            // Przełącz na UserPanel
            cardLayout.show(cardPanel, "User");

        } catch (LoginFailedException error) {
            JOptionPane.showMessageDialog(this, "Zle dane - nie udalo sie zalogowac");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(this, "Zle dane");
        }
    }

    // Metody dostępu do użytkownika i hasła
    public String getEmail() {
        return emailField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }
}