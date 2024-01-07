package banking_app.classes;

import banking_exceptions.*;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Menu extends JFrame {
    private final ConnectionManager manager;
    private JPanel mainPanel;
    private JPanel welcomePanel;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel welcomeLabel;
    private JPanel loginPanel;
    private JLabel emailLabel;
    private JFormattedTextField emailInputField;
    private JLabel passwordLabel;
    private JPasswordField passwordInputField;
    private JPanel registerPanel;
    private JLabel emailRegisterLabel;
    private JButton returnButton;
    private JButton confirmLoginButton;
    private JButton confirmRegisterButton;
    private JLabel nameRegisterLabel;
    private JFormattedTextField emailRegisterField;
    private JFormattedTextField nameRegisterField;
    private JLabel passwordRegisterLabel2;
    private JLabel passwordRegisterLabel;
    private JButton returnRegisterButton;
    private JPasswordField passwordRegisterField1;
    private JPasswordField passwordRegisterField2;
    private JLabel surnameRegisterLabel;
    private JFormattedTextField surnameRegisterField;

    public Menu(ConnectionManager m) {
        manager = m;


        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        setContentPane(mainPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "login");
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "register");
            }
        });
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(welcomePanel, "welcome");


        setTitle("Druzyna Bank");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "welcome");

            }
        });
        confirmLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailInputField.getText();
                char[] password = passwordInputField.getPassword();
                User user;
                try {
                    user = User.login(manager, email, password);
                    JOptionPane.showMessageDialog(Menu.this, "Zalogowano");

                } catch (LoginFailedException error) {
                    JOptionPane.showMessageDialog(Menu.this, "Zle dane");
                } catch (SQLException error) {
                    JOptionPane.showMessageDialog(Menu.this, "Zle dane");
                }
            }
        });
        returnRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "welcome");
            }
        });
        confirmRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailRegisterField.getText();
                String name = nameRegisterField.getText();
                String surname = surnameRegisterField.getText();
                char[] password = passwordRegisterField1.getPassword();
                char[] passwordRepeated = passwordRegisterField2.getPassword();
                User user;
                try {
                    user = User.register(manager, email, name, surname, password, passwordRepeated);
                } catch (OccupiedEmailException a) {
                    JOptionPane.showMessageDialog(Menu.this, "Email zostal uzyty");
                } catch (InvalidEmailException a) {
                    JOptionPane.showMessageDialog(Menu.this, "Niepoprawny email");
                } catch (InvalidPasswordException a) {
                    JOptionPane.showMessageDialog(Menu.this, "Niepoprawne haslo");
                } catch (PasswordMissmatchException a) {
                    JOptionPane.showMessageDialog(Menu.this, "Hasla nie sa takie same");
                } catch (InvalidNameException a) {
                    JOptionPane.showMessageDialog(Menu.this, "Zle dane w polu imie");
                } catch (SQLException a) {
                    JOptionPane.showMessageDialog(Menu.this, "Blad bazy");
                }
            }
        });
    }
}
