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
        frame.setSize(800, 600);
        ConnectionManager manager = new ConnectionManager();

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // Tworzenie poszczególnych kart
        //JPanel loginPanel = new JPanel(); // Dodaj elementy do loginPanel
        //JPanel registerPanel = new JPanel(); // Dodaj elementy do registerPanel
        //JPanel userPanel = new JPanel(); // Dodaj elementy do userPanel

        LoginPanel loginPanel = new LoginPanel(manager, cardLayout, cardPanel);
        RegisterPanel registerPanel = new RegisterPanel(manager, cardLayout, cardPanel);
        UserProfilePanel userProfilePanel = new UserProfilePanel(manager, cardLayout, cardPanel);
        ModifyProfilePanel modifyProfilePanel = new ModifyProfilePanel(manager, cardLayout, cardPanel);
        TransactionsPanel transactionsPanel = new TransactionsPanel(manager, cardLayout, cardPanel);
        AutomaticSavingsGui savingsPanel = new AutomaticSavingsGui(manager, cardLayout, cardPanel);
        CreateAutomaticSavingsPanel createSavingPanel = new CreateAutomaticSavingsPanel(manager, cardLayout, cardPanel);

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(registerPanel, "Register");
        cardPanel.add(userProfilePanel, "User");
        cardPanel.add(modifyProfilePanel, "ModifyProfile");
        cardPanel.add(transactionsPanel, "Transactions");
        cardPanel.add(savingsPanel, "SavingsPanel");
        cardPanel.add(createSavingPanel, "CreateSaving");



        // Logika przełączania kart
        // ...

        frame.add(cardPanel);
        frame.setVisible(true);
    }
}
