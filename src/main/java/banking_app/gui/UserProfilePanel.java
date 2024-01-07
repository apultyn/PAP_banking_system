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



    public UserProfilePanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel) {
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


        add(helloLabel);
        add(transactionsButton);
        add(contactsButton);
        add(transactionHistoryButton);
        add(modifyProfileButton);
        add(automaticSavingButton);

        modifyProfileButton.addActionListener(e -> handleModifyButton());
        automaticSavingButton.addActionListener(e -> handleAutomaticSavings());
    }

    public void setUser(User setted_user) {
        user = setted_user;
        helloLabel.setText("Hello " + user.getName());
    }

    public void  handleModifyButton() {
        ModifyProfilePanel modifyPanel = (ModifyProfilePanel) cardPanel.getComponent(3);
        modifyPanel.setUser(user);
        // Przełącz na UserPanel
        cardLayout.show(cardPanel, "ModifyProfile");
    }

    public void handleTransactionsButton() {
        TransactionsPanel transactionsPanel = (TransactionsPanel) cardPanel.getComponent(4);
        transactionsPanel.setUser(user);
        cardLayout.show(cardPanel, "Transactions");
    }

    public void handleAutomaticSavings() {
        AutomaticSavingsGui savingsPanel = (AutomaticSavingsGui) cardPanel.getComponent(5);
        savingsPanel.setUser(user);
        cardLayout.show(cardPanel, "SavingsPanel");
    }
}
