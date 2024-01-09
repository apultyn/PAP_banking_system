package banking_app.gui;

import banking_app.classes.User;
import banking_exceptions.LoginFailedException;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import static banking_app.gui.SwingUtilities.resetComponents;
import static banking_app.gui.SwingUtilities.addLabelAndComponent;

public class LoginPanel extends JPanel {
    private JLabel loginLabel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public LoginPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {

        this.setName(panelName);
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        add(loginLabel = new JLabel("Login"), gbc);
        loginLabel.setFont(new Font(loginLabel.getFont().getFontName(), Font.BOLD, 24));
        gbc.gridwidth = 1;
        gbc.gridy++;
        addLabelAndComponent(this, "E-mail:", emailField = new JTextField(20), gbc);
        addLabelAndComponent(this, "Password", passwordField = new JPasswordField(20), gbc);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        loginButton.addActionListener(e -> {
            handleLogin();
            passwordField.setText("");
        });
        registerButton.addActionListener(e -> {
            resetComponents(this);
            cardLayout.show(cardPanel, "Register");
        });
        add(loginButton, gbc);
        gbc.gridy++;
        add(registerButton, gbc);

//        JLabel userLabel = new JLabel("Email:");
//        emailField = new JTextField();
//        JLabel passwordLabel = new JLabel("Password:");
//        passwordField = new JPasswordField();
//
//        loginButton = new JButton("Login");
//        registerButton = new JButton("Register");
//
//        loginButton.addActionListener(e -> handleLogin());
//        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "Register"));
//
//        add(userLabel);
//        add(emailField);
//        add(passwordLabel);
//        add(passwordField);
//        add(loginButton);
//        add(registerButton);
    }

//    private void addLabelAndComponent(String labelText, Component component, GridBagConstraints gbc) {
//        add(new JLabel(labelText), gbc);
//        gbc.gridx++;
//        add(component, gbc);
//        gbc.gridx = 0;
//        gbc.gridy++;
//    }

    public void handleLogin() {
        String email = emailField.getText();
        char[] password = passwordField.getPassword();
        User user;
        try {
            user = User.login(manager, email, password);
            JOptionPane.showMessageDialog(this, "Logged in!");
            UserProfilePanel userPanel = (UserProfilePanel) SwingUtilities.findPanelByName(cardPanel, "User");
            if (userPanel != null) {
                userPanel.setUser(user);
                cardLayout.show(cardPanel, "User");
            }
        } catch (LoginFailedException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
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