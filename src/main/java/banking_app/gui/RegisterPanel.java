package banking_app.gui;

import banking_app.classes.User;
import banking_exceptions.*;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static banking_app.gui.SwingUtilities.resetComponents;

public class RegisterPanel extends JPanel {
    private final JLabel registerLabel;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField passwordRepeatedField;
    private JButton registerButton;
    private JButton backButton;
    private ConnectionManager manager;

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
        addLabelAndComponent("Name:", firstNameField = new JTextField(20), gbc);
        addLabelAndComponent("Last name:", lastNameField = new JTextField(20), gbc);
        addLabelAndComponent("E-mal:", emailField = new JTextField(20), gbc);
        addLabelAndComponent("Password:", passwordField = new JPasswordField(20), gbc);
        addLabelAndComponent("Repeat password:", passwordRepeatedField = new JPasswordField(20), gbc);
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        registerButton = new JButton("Register");
        backButton = new JButton("Back");
        registerButton.addActionListener(e -> {
            handleRegister();
            resetComponents(new ArrayList<Component>(Arrays.asList(passwordField, passwordRepeatedField)));
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

    private void addLabelAndComponent(String labelText, Component component, GridBagConstraints gbc) {
        add(new JLabel(labelText), gbc);
        gbc.gridx++;
        add(component, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

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
