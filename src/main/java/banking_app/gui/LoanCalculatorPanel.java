package banking_app.gui;

import banking_app.classes.LoanCalc;

import javax.swing.*;
import java.awt.*;

public class LoanCalculatorPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField amountField, termField, rateField;
    private JButton confirmButton;
    private JLabel outcomeLabelM, outcomeLabelT, welcomeLabel;
    public LoanCalculatorPanel(CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.setName(panelName);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        welcomeLabel = new JLabel("Loan calculator");
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(20f));
        add(welcomeLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;

        addLabelAndComponent("Enter how much you want to loan: ", amountField = new JTextField(20), gbc);
        addLabelAndComponent("Enter for how many years are you loaning: ", termField = new JTextField(20), gbc);
        addLabelAndComponent("Enter the rate (max 20%):", rateField = new JTextField(20), gbc);

        outcomeLabelM = new JLabel("Monthly payment: ");
        add(outcomeLabelM, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx++;
        outcomeLabelT = new JLabel("Total payment: ");
        add(outcomeLabelT, gbc);

        gbc.gridy++;
        gbc.gridx = 0;

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e->calcLoan());
        add(confirmButton, gbc);
        gbc.gridy++;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e-> cardLayout.show(cardPanel, "User"));
        add(backButton, gbc);
    }
    public void calcLoan() {
        String amount = amountField.getText();
        String loanTerm = termField.getText();
        String rate = rateField.getText();
        try {
            double monthly = LoanCalc.calculateLoanMonthly(amount, loanTerm, rate);
            outcomeLabelM.setText(String.format("Monthly payment: %.2f pln", monthly));
            outcomeLabelT.setText(String.format("Total payment: %.2f pln",
                    LoanCalc.calculateLoanTotal(amount, loanTerm, rate)));
        } catch (NumberFormatException a) {
            JOptionPane.showMessageDialog(this, "Wrong input data");
        }
    }

    private void addLabelAndComponent(String labelText, Component component, GridBagConstraints gbc) {
        add(new JLabel(labelText), gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(component, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }
}
