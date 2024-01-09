package banking_app.gui;

import banking_app.classes.User;
import banking_exceptions.*;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ModifyProfilePanel extends JPanel {
    private JButton modifyNameButton;
    private JButton returnButton;
    private JButton modifySurnameButton;
    private JButton modifyEmailButton;
    private JButton modifyPasswordButton;
    private User user;
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public ModifyProfilePanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.manager = manager;
        this.setName(panelName);
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        returnButton = new JButton("Back");
        modifyNameButton = new JButton("Modify Name");
        modifySurnameButton = new JButton("Modify Surname");
        modifyEmailButton = new JButton("Modify Email");
        modifyPasswordButton = new JButton("Modify Password");

        modifyNameButton.addActionListener(e -> openModifyDialog("Name"));
        modifySurnameButton.addActionListener(e -> openModifyDialog("Surname"));
        modifyEmailButton.addActionListener(e -> openModifyDialog("Email"));
        modifyPasswordButton.addActionListener(e -> openModifyDialog("Password")); // No need to show old password

        add(modifyNameButton);
        add(modifySurnameButton);
        add(modifyEmailButton);
        add(modifyPasswordButton);
        add(returnButton);

        returnButton.addActionListener(e -> cardLayout.show(cardPanel, "User"));
    }

    private void openModifyDialog(String field) {
        // Create and display dialog
        JDialog dialog = new JDialog();
        dialog.setLayout(new GridLayout(0, 2));
        dialog.add(new JLabel("Old " + field + ":"));
        JTextField oldField = new JTextField();
        dialog.add(oldField);

        dialog.add(new JLabel("New " + field + ":"));
        JTextField newField = new JTextField();
        dialog.add(newField);

        // Additional field for password confirmation
        JTextField confirmField = new JTextField();;
        if (field.equals("Password")) {
            dialog.add(new JLabel("Confirm New Password:"));
            dialog.add(confirmField);
        }

        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        dialog.add(confirmButton);
        dialog.add(cancelButton);

        // Confirm button action
        confirmButton.addActionListener(e -> {
            try {
                updateUserData(field, oldField.getText(), newField.getText(), field.equals("Password") ? confirmField.getText() : "");
                JOptionPane.showMessageDialog(dialog, "Modification of " + field + " successful.");
                setUser(manager.findUser(user.getEmail()));
                dialog.dispose();
            } catch (MissingInformationException ex) {
                JOptionPane.showMessageDialog(dialog, "Missing information!");
            } catch (DataMissmatchException ex) {
                JOptionPane.showMessageDialog(dialog, "Wrong old value!");
            } catch (RepeatedDataException ex) {
                JOptionPane.showMessageDialog(dialog, "New value can't be the same as old!");
            } catch (InvalidNameException ex) {
                JOptionPane.showMessageDialog(dialog, "New value can't contain spaces!");
            } catch (InvalidEmailException ex) {
                JOptionPane.showMessageDialog(dialog, "New email is in wrong format!");
            } catch (InvalidPasswordException ex) {
                JOptionPane.showMessageDialog(dialog, "New password is incorrect!");
            } catch (PasswordMissmatchException ex) {
                JOptionPane.showMessageDialog(dialog, "New password not repeated correctly!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database error!");
            }
        });

        // Cancel button action
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setVisible(true);
    }
    public void setUser(User setted_user) {
        user = setted_user;
    }
    private void updateUserData(String field, String oldValue, String newValue, String confirmValue) throws
            InvalidNameException, SQLException, RepeatedDataException, MissingInformationException, DataMissmatchException, InvalidPasswordException, PasswordMissmatchException, InvalidEmailException {
        // Update user data based on field
        switch (field) {
            case "Name":
                user.updateFirstName(manager, oldValue, newValue);
                break;
            case "Surname":
                user.updateSurname(manager, oldValue, newValue);
                break;
            case "Email":
                user.updateEmail(manager, oldValue, newValue);
                break;
            case "Password":
                user.updatePassword(manager, oldValue, newValue, confirmValue);
                break;
        }
    }
    public static void main(String[] args) throws SQLException {
        ConnectionManager manager = new ConnectionManager();
        User user = manager.findUser("przykladowy.mail@pw.edu.pl");
//        Deposit deposit = new Deposit(0, "czwarte", new BigDecimal(19.94), new BigDecimal(2),
//                1000000000000047L, new Date(2024, 1, 10), new Date(2024, 10, 11));
//        deposit.createDeposit(manager);
        ModifyProfilePanel modifyProfilePanel = new ModifyProfilePanel(manager, null, null, "ModifyProfilePanel");
        modifyProfilePanel.setUser(user);
        JFrame frame = new JFrame("Bank Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(modifyProfilePanel);
        frame.setVisible(true);
    }
}
