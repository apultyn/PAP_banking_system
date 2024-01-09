package banking_app.gui;

import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;

public class UserProfilePanel extends JPanel {
    private User user;

    private JLabel helloLabel;

    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;



    public UserProfilePanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.setName(panelName);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.manager = manager;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        helloLabel = new JLabel();

        JButton transactionsButton = new JButton("Transactions");
        JButton contactsButton = new JButton("Contacts");
        JButton transactionHistoryButton = new JButton("Transaction History");
        JButton modifyProfileButton = new JButton("Modify data");
        JButton automaticSavingButton = new JButton("Automatic Savings");
        JButton accountsButton = new JButton("My Accounts");
        JButton standingOrdersButton = new JButton("Standing Orders");
        JButton loanCalculatorButton = new JButton("Loan Calculator");
        JButton loanButton = new JButton("Loans");

        add(helloLabel);
        add(transactionsButton);
        add(contactsButton);
        add(transactionHistoryButton);
        add(modifyProfileButton);
        add(automaticSavingButton);
        add(accountsButton);
        add(standingOrdersButton);
        add(loanCalculatorButton);
        add(loanButton);

        modifyProfileButton.addActionListener(e -> handleModifyButton());
        automaticSavingButton.addActionListener(e -> handleAutomaticSavings());
        accountsButton.addActionListener(e -> handleAccountsButton());
        standingOrdersButton.addActionListener(e->handleStandingOrders());
        loanCalculatorButton.addActionListener(e->handleLoanCalculator());
        loanButton.addActionListener(e->handleLoansButton());

    }

    public void setUser(User setted_user) {
        user = setted_user;
        helloLabel.setText("Hello " + user.getName());
    }

    public void  handleModifyButton() {
        ModifyProfilePanel modifyPanel = (ModifyProfilePanel) SwingUtilities.findPanelByName(cardPanel, "ModifyPanel");
        if (modifyPanel != null){
            modifyPanel.setUser(user);
            cardLayout.show(cardPanel, "ModifyProfile");
        }

    }

    public void handleTransactionsButton() {
        TransactionsPanel transactionsPanel = (TransactionsPanel) SwingUtilities.findPanelByName(cardPanel, "Transactions");
        if (transactionsPanel != null) {
            transactionsPanel.setUser(user);
            cardLayout.show(cardPanel, "Transactions");
        }
    }

    public void handleAutomaticSavings() {
        AutomaticSavingsGui savingsPanel = (AutomaticSavingsGui) SwingUtilities.findPanelByName(cardPanel, "SavingsPanel");
        if (savingsPanel != null) {
            savingsPanel.setUser(user);
            cardLayout.show(cardPanel, "SavingsPanel");
        }
    }

    public void handleAccountsButton() {
        AccountsPanel accountsPanel = (AccountsPanel) SwingUtilities.findPanelByName(cardPanel, "Accounts");
        if (accountsPanel != null){
            accountsPanel.setUser(user);
            cardLayout.show(cardPanel, "Accounts");
        }
    }
    public void handleStandingOrders() {
        StandingOrdersPanel savingsPanel = (StandingOrdersPanel) SwingUtilities.findPanelByName(cardPanel, "StandingOrders");;
        if (savingsPanel != null) {
            savingsPanel.setUser(user);
            cardLayout.show(cardPanel, "StandingOrders");
        }
    }

    public void handleLoanCalculator() {
        LoanCalculatorPanel savingsPanel = (LoanCalculatorPanel) SwingUtilities.findPanelByName(cardPanel, "LoanCalculator");
        cardLayout.show(cardPanel, "LoanCalculator");
    }

    public void handleLoansButton() {
        LoansPanel loansPanel = (LoansPanel) SwingUtilities.findPanelByName(cardPanel, "Loans");
        if (loansPanel != null) {
            loansPanel.setUser(user);
            cardLayout.show(cardPanel, "Loans");
        }
    }
}
