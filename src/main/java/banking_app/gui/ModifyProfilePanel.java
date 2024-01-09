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
    private JLabel name;
    private JLabel surname;
    private JLabel email;
    private User user;
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public ModifyProfilePanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.manager = manager;
        this.setName(panelName);
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        setLayout(new GridBagLayout()); // Ustawienie GridBagLayout dla głównego panelu
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10); // Odstępy wokół komponentów

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("Modify Data");
        headerPanel.add(headerLabel);
        headerLabel.setFont(new Font(headerLabel.getFont().getName(), Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        returnButton = new JButton("Back");
        modifyNameButton = new JButton("Modify Name");
        modifySurnameButton = new JButton("Modify Surname");
        modifyEmailButton = new JButton("Modify Email");
        modifyPasswordButton = new JButton("Modify Password");

        name = new JLabel();
        surname = new JLabel();
        email = new JLabel();

        JPanel namePanel = createLinePanel(name, modifyNameButton);
        JPanel surnamePanel = createLinePanel(surname, modifySurnameButton);
        JPanel emailPanel = createLinePanel(email, modifyEmailButton);
        JPanel passwordPanel = createLinePanel(new JLabel(), modifyPasswordButton); // No label for password

        returnButton = new JButton("Back");
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        modifyNameButton.addActionListener(e -> openModifyDialog("Name"));
        modifySurnameButton.addActionListener(e -> openModifyDialog("Surname"));
        modifyEmailButton.addActionListener(e -> openModifyDialog("Email"));
        modifyPasswordButton.addActionListener(e -> openModifyDialog("Password")); // No need to show old password

        returnButton.addActionListener(e -> cardLayout.show(cardPanel, "User"));

        gbc.anchor = GridBagConstraints.CENTER;
        add(headerPanel, gbc);
        add(namePanel, gbc);
        add(surnamePanel, gbc);
        add(emailPanel, gbc);
        add(passwordPanel, gbc);
        add(returnButton, gbc);
    }

    private JPanel createLinePanel(JComponent label, JButton button) {
        JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        label.setPreferredSize(new Dimension(200, 20)); // Ustawienie preferowanych wymiarów dla etykiety
        button.setPreferredSize(new Dimension(150, 20)); // Ustawienie preferowanych wymiarów dla przycisku
        linePanel.add(label);
        linePanel.add(button);
        return linePanel;
    }


    private void openModifyDialog(String field) {
        // Create and display dialog
        Dialog dialog = new JDialog();
        dialog.setSize(300, 200);
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

        //dialog.pack();
        dialog.setLocationRelativeTo(SwingUtilities.findPanelByName(cardPanel, "ModifyPanel"));
        dialog.setVisible(true);

    }
    public void setUser(User setted_user) {
        user = setted_user;
        name.setText(user.getName());
        surname.setText(user.getSurname());
        email.setText(user.getEmail());
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
//    public static void main(String[] args) throws SQLException {
//        ConnectionManager manager = new ConnectionManager();
//        User user = manager.findUser("przykladowy.mail@pw.edu.pl");
////        Deposit deposit = new Deposit(0, "czwarte", new BigDecimal(19.94), new BigDecimal(2),
////                1000000000000047L, new Date(2024, 1, 10), new Date(2024, 10, 11));
////        deposit.createDeposit(manager);
//        ModifyProfilePanel modifyProfilePanel = new ModifyProfilePanel(manager, null, null, "ModifyProfilePanel");
//        modifyProfilePanel.setUser(user);
//        JFrame frame = new JFrame("Bank Application");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 600);
//        frame.add(modifyProfilePanel);
//        frame.setVisible(true);
//    }
}
