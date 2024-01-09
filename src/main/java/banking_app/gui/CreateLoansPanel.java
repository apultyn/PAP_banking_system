package banking_app.gui;

import banking_app.classes.Loan;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class CreateLoansPanel extends JPanel {
    private User user;
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField amountField;
    private JTextField rateField;
    private JTextField endDateField;
    private JTextField ownerAccField;

    private JButton backButton, confirmButton;
    public CreateLoansPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.setName(panelName);
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Create new loan"));

        backButton = new JButton("Back");
        backButton.addActionListener(e-> cardLayout.show(cardPanel, "Loans"));
        add(backButton);

        add(new JLabel("Write the amount of the loan"));

        amountField = new JTextField(10);
        add(amountField);

        add(new JLabel("Write the rate of the loan"));

        rateField = new JTextField(20);
        add(rateField);

        add(new JLabel("Write the ending date"));

        endDateField = new JTextField(20);
        add(endDateField);

        add(new JLabel("Write the account number to which you want the loan"));

        ownerAccField = new JTextField(20);
        add(ownerAccField);

        confirmButton = new JButton("confirm");
        confirmButton.addActionListener(e-> registerAutomatic());
        add(confirmButton);

    }
    public void registerAutomatic() {
        String amount = amountField.getText();
        String rate = rateField.getText();
        String endDate = endDateField.getText();
        String ownerId = ownerAccField.getText();
        try {
            Loan.createLoan(manager, amount, rate, endDate, ownerId, user);
            manager.addBalance(Long.parseLong(ownerId), BigDecimal.valueOf(Double.parseDouble(amount)));
            JOptionPane.showMessageDialog(this, "Created");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong input");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Bd fault");
        }
    }
    public void setUser(User u) {
        user = u;
    }
}
