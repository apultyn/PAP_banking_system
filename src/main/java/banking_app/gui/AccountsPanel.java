package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;


public class AccountsPanel extends JPanel {
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JList<String> accountsList;
    private DefaultListModel<String> accountsListModel; // Model dla JList
    private User user;
    public AccountsPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName){
        this.setName(panelName);
        this.manager = manager;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;


        JButton returnButton = new JButton("Back");
        accountsListModel = new DefaultListModel<>();
        accountsList = new JList<>(accountsListModel);
        JScrollPane scrollPane = new JScrollPane(accountsList);
        accountsList.setFixedCellHeight(30); // Wysokość każdego elementu listy
        //accountsList.setFixedCellWidth(150); // Szer

        add(scrollPane); // Dodaj JScrollPane z JList do panelu
        add(returnButton);

        returnButton.addActionListener(e -> cardLayout.show(cardPanel, "User"));
    }

    public void showAccounts() {
        accountsListModel.clear();
        try {
            List<Account> accounts = manager.findUsersAccounts(user.getId());
            for (Account account : accounts) {
                String account_info = "Name: " + account.getName() + "Balance: " + account.getBalance();
                accountsListModel.addElement(account_info);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Accounts not found");
        }
    }

    public void setUser(User user) {
        this.user = user;
        showAccounts();
    }
}
