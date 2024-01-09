package banking_app.gui;

import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainMenu extends JFrame {
    private ConnectionManager manager;

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
        ModifyProfilePanel modifyProfilePanel = new ModifyProfilePanel(manager, cardLayout, cardPanel, "ModifyPanel");
        CreateAccountPanel createAccountPanel = new CreateAccountPanel(manager, cardLayout, cardPanel, "CreateAccount");
        ContactsPanel contactsPanel = new ContactsPanel(manager, cardLayout, cardPanel, "Contacts");

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(registerPanel, "Register");
        cardPanel.add(userProfilePanel, "User");
        cardPanel.add(transactionsPanel, "Transactions");
        cardPanel.add(savingsPanel, "SavingsPanel");
        cardPanel.add(createSavingPanel, "CreateSaving");
        cardPanel.add(accountsPanel, "Accounts");
        cardPanel.add(standingOrdersPanel, "StandingOrders");
        cardPanel.add(createStandingOrdersPanel, "CreateStanding");
        cardPanel.add(modifyProfilePanel, "ModifyPanel");
        cardPanel.add(createAccountPanel, "CreateAccount");
        cardPanel.add(contactsPanel, "Contacts");

        frame.add(cardPanel);
        frame.setVisible(true);
    }

}
