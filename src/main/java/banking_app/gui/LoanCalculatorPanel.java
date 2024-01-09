package banking_app.gui;

import banking_app.classes.LoanCalc;

import javax.swing.*;
import java.awt.*;

public class LoanCalculatorPanel extends JPanel {
    private double inputAmount;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField amountField, termField, rateField;
    private JButton confirmButton;
    private JLabel outcomeLabel;
    public LoanCalculatorPanel(CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.setName(panelName);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new Label("Loan calculator"));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e-> cardLayout.show(cardPanel, "User"));
        add(backButton);

        add(new Label("Enter how much you want to loan"));
        amountField = new JTextField(20);
        add(amountField);

        add(new Label("Enter for how many years are you loaning"));
        termField = new JTextField(20);
        add(termField);

        add(new Label("Enter the rate (max 10%)"));
        rateField = new JTextField(20);
        add(rateField);

        outcomeLabel = new JLabel("Outcome: ");
        add(outcomeLabel);

        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e->calcLoan());
        add(confirmButton);
    }
    public void calcLoan() {
        String amount = amountField.getText();
        String loanTerm = termField.getText();
        String rate = rateField.getText();
        try {
            double monthly = LoanCalc.calculateLoanMonthly(amount, loanTerm, rate);
            outcomeLabel.setText("Monthly payment: " +
                    String.valueOf(monthly) + " Total: " +
                    String.valueOf(LoanCalc.calculateLoanTotal(amount, loanTerm, rate)));
        } catch (NumberFormatException a) {
            JOptionPane.showMessageDialog(this, "Wrong input data");
        }
    }
}
