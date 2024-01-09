package banking_app.gui;

import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainMenu extends JFrame {
    private ConnectionManager manager;
    public MainMenu() {
        String[] arr = new String[]{};
        try {
            main(arr);
        } catch (SQLException a) {
            System.out.println("a");
        }
    }
    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame("Bank Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        ConnectionManager manager = new ConnectionManager();

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // Tworzenie poszczególnych kart
        //JPanel loginPanel = new JPanel(); // Dodaj elementy do loginPanel
        //JPanel registerPanel = new JPanel(); // Dodaj elementy do registerPanel
        //JPanel userPanel = new JPanel(); // Dodaj elementy do userPanel

        LoginPanel loginPanel = new LoginPanel(manager, cardLayout, cardPanel, "Login");
        RegisterPanel registerPanel = new RegisterPanel(manager, cardLayout, cardPanel, "Register");
        UserProfilePanel userProfilePanel = new UserProfilePanel(manager, cardLayout, cardPanel, "User");
        TransactionsPanel transactionsPanel = new TransactionsPanel(manager, cardLayout, cardPanel, "Transactions");
        AutomaticSavingsGui savingsPanel = new AutomaticSavingsGui(manager, cardLayout, cardPanel, "SavingsPanel");
        CreateAutomaticSavingsPanel createSavingPanel = new CreateAutomaticSavingsPanel(manager, cardLayout, cardPanel, "CreateSaving");
        AccountsPanel accountsPanel = new AccountsPanel(manager, cardLayout, cardPanel, "Accounts");
        StandingOrdersPanel standingOrdersPanel = new StandingOrdersPanel(manager, cardLayout, cardPanel, "StandingOrders");
        CreateStandingOrdersPanel createStandingOrdersPanel = new CreateStandingOrdersPanel(manager, cardLayout, cardPanel, "CreateStanding");

        LoanCalculatorPanel loanCalculator = new LoanCalculatorPanel(cardLayout, cardPanel, "LoanCalculator");
        LoansPanel loansPanel = new LoansPanel(manager, cardLayout, cardPanel, "Loans");
        CreateLoansPanel createLoansPanel = new CreateLoansPanel(manager, cardLayout, cardPanel, "CreateLoans");

        ModifyProfilePanel modifyProfilePanel = new ModifyProfilePanel(manager, cardLayout, cardPanel, "ModifyPanel");
        CreateAccountPanel createAccountPanel = new CreateAccountPanel(manager, cardLayout, cardPanel, "CreateAccount");

        ContactsPanel contactsPanel = new ContactsPanel(manager, cardLayout, cardPanel, "Contacts");

        DepositPanel depositPanel = new DepositPanel(manager, cardLayout, cardPanel, "Deposit");


        cardPanel.add(loginPanel, "Login");
        cardPanel.add(registerPanel, "Register");
        cardPanel.add(userProfilePanel, "User");
        cardPanel.add(transactionsPanel, "Transactions");
        cardPanel.add(savingsPanel, "SavingsPanel");
        cardPanel.add(createSavingPanel, "CreateSaving");
        cardPanel.add(accountsPanel, "Accounts");
        cardPanel.add(standingOrdersPanel, "StandingOrders");
        cardPanel.add(createStandingOrdersPanel, "CreateStanding");

        cardPanel.add(loanCalculator, "LoanCalculator");
        cardPanel.add(loansPanel, "Loans");
        cardPanel.add(createLoansPanel, "CreateLoans");



        // Logika przełączania kart
        // ...
        cardPanel.add(modifyProfilePanel, "ModifyPanel");
        cardPanel.add(createAccountPanel, "CreateAccount");
        cardPanel.add(contactsPanel, "Contacts");
        cardPanel.add(depositPanel, "Deposit");

        frame.add(cardPanel);
        frame.setVisible(true);
    }

}
