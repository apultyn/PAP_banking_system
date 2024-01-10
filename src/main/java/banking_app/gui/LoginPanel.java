package banking_app.gui;

import banking_app.classes.EmailSender;
import banking_app.classes.User;
import banking_exceptions.*;
import connections.ConnectionManager;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Random;

import static banking_app.gui.SwingUtilities.addLabelAndComponent;
import static banking_app.gui.SwingUtilities.resetComponents;

public class LoginPanel extends JPanel {
    private final JLabel loginLabel;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final ConnectionManager manager;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private String resetPassword;

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
        gbc.anchor = GridBagConstraints.WEST;
        addLabelAndComponent(this, "E-mail:", emailField = new JTextField(20), gbc);
        addLabelAndComponent(this, "Password", passwordField = new JPasswordField(20), gbc);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        JButton resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.addActionListener(e -> createEmailInputDialog());
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
        gbc.gridy++;
        add(resetPasswordButton, gbc);
    }

    public void handleLogin() {
        String email = emailField.getText();
        char[] password = passwordField.getPassword();
        User user;
        try {
            user = User.login(manager, email, password);

            int attempts = 3;
            while (attempts > 0) {
                String PIN = JOptionPane.showInputDialog(this, "Enter your PIN: (Attempts left: " + attempts + ")");
                if (PIN == null)
                    break;
                else if (!validatePIN(user, PIN)) {
                    attempts--;
                    JOptionPane.showMessageDialog(this, "Incorrect PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Logged in!");
                    UserProfilePanel userPanel = (UserProfilePanel) SwingUtilities.findPanelByName(cardPanel, "User");
                    if (userPanel != null) {
                        new EmailSender(manager).sendLoginInfo(user);
                        userPanel.setUser(user);
                        cardLayout.show(cardPanel, "User");
                    }
                    break;
                }
            }
        } catch (LoginFailedException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validatePIN(User user, String PIN) {
        return PIN.equals(user.getPin());
    }

    private void createEmailInputDialog() {
        JDialog emailDialog = new JDialog();
        emailDialog.setLayout(new FlowLayout());
        JTextField emailInputField = new JTextField(20);
        JLabel emailLabel = new JLabel("Enter email");
        JButton goBackButton = new JButton("Back");
        JButton sendResetCodeButton = new JButton("Send Reset Code");

        emailDialog.add(emailLabel);
        emailDialog.add(emailInputField);
        emailDialog.add(goBackButton);
        emailDialog.add(sendResetCodeButton);
        emailDialog.pack();
        emailDialog.setVisible(true);

        goBackButton.addActionListener(e -> emailDialog.dispose());
        sendResetCodeButton.addActionListener(e -> {
            String input = emailInputField.getText();
            try {
                User user = manager.findUser(input);
                if (user == null)
                    JOptionPane.showMessageDialog(emailDialog, "No account with such email!", "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    resetPassword = createNewResetCode();
                    new EmailSender(manager).sendResetCode(input, resetPassword);
                    emailDialog.dispose();
                    createCodeInputDialog(user);
                }
            } catch (SQLException | MessagingException ex) {
                throw new RuntimeException(ex);
            }
        });
        emailDialog.setLocationRelativeTo(SwingUtilities.findPanelByName(cardPanel, "Login"));
    }

    private String createNewResetCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }

    private void createCodeInputDialog(User user) {
        JDialog codeDialog = new JDialog();
        codeDialog.setLayout(new FlowLayout());
        JTextField codeInputField = new JTextField(20);
        JLabel textLabel = new JLabel("Enter code you received at your email");
        JButton goBackButton = new JButton("Back");
        JButton submitCodeButton = new JButton("Submit");

        codeDialog.add(textLabel);
        codeDialog.add(codeInputField);
        codeDialog.add(goBackButton);
        codeDialog.add(submitCodeButton);
        codeDialog.pack();
        codeDialog.setVisible(true);

        goBackButton.addActionListener(e -> codeDialog.dispose());
        submitCodeButton.addActionListener(e -> {
            if (!codeInputField.getText().equals(resetPassword))
                JOptionPane.showMessageDialog(codeDialog, "Wrong code", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                codeDialog.dispose();
                createNewPasswordDialog(user);
            }
        });
    }

    private void createNewPasswordDialog(User user) {
        JDialog newPasswordDialog = new JDialog();
        newPasswordDialog.setLayout(new FlowLayout());
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        JButton goBackButton = new JButton("Back");
        JButton submitNewPasswordButton = new JButton("Submit");

        JLabel newPasswordLabel = new JLabel("New password");
        JLabel repNewPasswordLabel = new JLabel("Repeat new password");

        newPasswordDialog.add(newPasswordLabel);
        newPasswordDialog.add(newPasswordField);
        newPasswordDialog.add(repNewPasswordLabel);
        newPasswordDialog.add(confirmPasswordField);
        newPasswordDialog.add(goBackButton);
        newPasswordDialog.add(submitNewPasswordButton);
        newPasswordDialog.pack();
        newPasswordDialog.setVisible(true);

        goBackButton.addActionListener(e -> newPasswordDialog.dispose());
        submitNewPasswordButton.addActionListener(e -> {
            String newPassword = String.valueOf(newPasswordField.getPassword());
            String repNewPassword = String.valueOf(confirmPasswordField.getPassword());
            try {
                System.out.println(newPassword + " " + repNewPassword);
                user.updatePassword(manager, user.getPassword(), newPassword, repNewPassword);
                JOptionPane.showMessageDialog(newPasswordDialog, "Password changed successfully");
                newPasswordDialog.dispose();
            } catch (RepeatedDataException | InvalidPasswordException | MissingInformationException |
                    PasswordMissmatchException | DataMissmatchException ex) {
                JOptionPane.showMessageDialog(newPasswordDialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}