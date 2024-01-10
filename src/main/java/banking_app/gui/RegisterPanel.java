package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.EmailSender;
import banking_app.classes.User;
import banking_exceptions.*;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static banking_app.gui.SwingUtilities.addLabelAndComponent;
import static banking_app.gui.SwingUtilities.resetComponents;

public class RegisterPanel extends JPanel {
    private final JLabel registerLabel;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField emailField;
    private final JTextField accountNameField;
    private final JTextField transferLimit;
    private final JPasswordField passwordField;
    private final JPasswordField passwordRepeatedField;
    private final JTextField pinField;
    private final JTextField repPinField;
    private final JButton registerButton;
    private final JButton backButton;
    private final ConnectionManager manager;

    public RegisterPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
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

        add(registerLabel = new JLabel("Register"), gbc);
        registerLabel.setFont(new Font(registerLabel.getFont().getFontName(), Font.BOLD, 24));
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        addLabelAndComponent(this, "Name:", firstNameField = new JTextField(20), gbc);
        addLabelAndComponent(this, "Last name:", lastNameField = new JTextField(20), gbc);
        addLabelAndComponent(this, "E-mail:", emailField = new JTextField(20), gbc);
        addLabelAndComponent(this, "Password:", passwordField = new JPasswordField(20), gbc);
        addLabelAndComponent(this, "Repeat password:", passwordRepeatedField = new JPasswordField(20), gbc);
        addLabelAndComponent(this, "Pin:", pinField = new JTextField(20), gbc);
        addLabelAndComponent(this, "Repeat pin:", repPinField = new JTextField(20), gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;

        JLabel headlineLabel = new JLabel("Create your first account");
        headlineLabel.setFont(headlineLabel.getFont().deriveFont(Font.BOLD, 24));
        add(headlineLabel, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        addLabelAndComponent(this, "Account name:", accountNameField = new JTextField(20), gbc);
        addLabelAndComponent(this, "Transfer limit:", transferLimit = new JTextField(20), gbc);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        registerButton = new JButton("Register");
        backButton = new JButton("Back");
        registerButton.addActionListener(e -> {
            handleRegister();
            resetComponents(new ArrayList<>(Arrays.asList(passwordField, passwordRepeatedField)));
        });
        backButton.addActionListener(e -> {
            resetComponents(this);
            cardLayout.show(cardPanel, "Login");
        });

        add(registerButton, gbc);
        gbc.gridy++;
        add(backButton, gbc);
    }

    private void handleRegister() {
        String email = emailField.getText();
        String name = firstNameField.getText();
        String surname = lastNameField.getText();
        char[] password = passwordField.getPassword();
        char[] passwordRepeated = passwordRepeatedField.getPassword();
        String pin = pinField.getText();
        String repPin = repPinField.getText();
        String accName = accountNameField.getText();
        String transferLimitInput = transferLimit.getText();
        try {
            User user = User.createNewUser(manager, email, name, surname, password, passwordRepeated, pin, repPin);
            Account account = User.createAccount(manager, user, accName, transferLimitInput);
            String verificationNumber = generateConfirmationNumber();
            new EmailSender(manager).sendVerificationNumber(verificationNumber, user);

            int attempts = 3;
            while (attempts > 0) {
                String verificationNumberInput = JOptionPane.showInputDialog(this, "A 6-digit verification code has been sent to your email.\nEnter verification code: (Attempts left: " + attempts + ")");
                if (verificationNumberInput == null)
                    break;
                else if (!verificationNumberInput.equals(verificationNumber)) {
                    attempts--;
                    JOptionPane.showMessageDialog(this, "Incorrect code!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Registered successfully!");
                    User.register(manager, user, account);
                    resetComponents(this);
                    break;
                }
            }
            cardLayout.show(cardPanel, "Login");
        } catch (InvalidNameException | InvalidAmountException | InvalidPasswordException | InvalidEmailException |
                 OccupiedEmailException | InvalidPinException | PasswordMissmatchException | DataMissmatchException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateConfirmationNumber() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
