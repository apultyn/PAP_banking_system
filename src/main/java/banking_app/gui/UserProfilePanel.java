package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.Transaction;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserProfilePanel extends JPanel {
    private User user;
    private JLabel helloLabel;
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JComboBox<String> accountComboBox;
    private Account currentAccount;
    private JLabel accountDetailsLabel;
    private JLabel balanceLabel;
    private JPanel menuPanel;
    private JPanel contentPanel;

    private JTable transactionHistoryTable;
    private TransactionsHistoryModel transactionHistoryModel;

    public UserProfilePanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.setName(panelName);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.manager = manager;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        transactionHistoryModel = new TransactionsHistoryModel(new ArrayList<>());
        transactionHistoryTable = new JTable(transactionHistoryModel);
        JScrollPane scrollPane = new JScrollPane(transactionHistoryTable);

        menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton transactionsButton = new JButton("Transactions");
        JButton contactsButton = new JButton("Contacts");
        JButton transactionHistoryButton = new JButton("Transaction History");
        JButton modifyProfileButton = new JButton("Modify data");
        JButton automaticSavingButton = new JButton("Automatic Savings");
        JButton accountsButton = new JButton("My Accounts");
        JButton standingOrdersButton = new JButton("Standing Orders");

        menuPanel.add(transactionsButton);
        menuPanel.add(contactsButton);
        menuPanel.add(transactionHistoryButton);
        menuPanel.add(modifyProfileButton);
        menuPanel.add(automaticSavingButton);
        menuPanel.add(accountsButton);
        menuPanel.add(standingOrdersButton);

        modifyProfileButton.addActionListener(e -> handleModifyButton());
        automaticSavingButton.addActionListener(e -> handleAutomaticSavings());
        accountsButton.addActionListener(e -> handleAccountsButton());
        standingOrdersButton.addActionListener(e->handleStandingOrders());
        transactionsButton.addActionListener(e -> {
            try {
                handleTransactionsButton();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        setLayout(new BorderLayout());
        add(menuPanel, BorderLayout.NORTH); // Add the menu panel at the top

        helloLabel = new JLabel();
        accountComboBox = new JComboBox<>();
        accountDetailsLabel = new JLabel();
        balanceLabel = new JLabel();

        helloLabel.setFont(helloLabel.getFont().deriveFont(20f));
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(16f));

        accountComboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        accountComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        contentPanel.add(helloLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(accountComboBox);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(accountDetailsLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(balanceLabel);

        accountComboBox.addActionListener(e -> onAccountSelected());

        setLayout(new BorderLayout());
        add(menuPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

    }

    public void setUser(User setted_user) {
        user = setted_user;
        helloLabel.setText("Hello " + user.getName());
        try {
            List<Account> accounts = manager.findUsersAccounts(user.getId());
            setAccounts(accounts);
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, e);
        }

    }

    public void setAccounts(List<Account> accounts) {
        accountComboBox.removeAllItems();
        for (Account account : accounts) {
            String item = account.getName() + " - " + account.getAccountId();
            accountComboBox.addItem(item);
        }
    }

    private void onAccountSelected() {
        String selected = (String) accountComboBox.getSelectedItem();
        if (selected != null) {
            String[] parts = selected.split(" - ");
            String accountName = parts[0];
            String accountId = parts.length > 1 ? parts[1] : "";

            try {
                currentAccount = manager.findAccount(Long.parseLong(accountId));
                String account_info = "Choosen account: " + currentAccount.getName() + ", Account ID: " + currentAccount.getAccountId();
                accountDetailsLabel.setText(account_info);
                String balance_info = String.format("Balance: %.2f zł", currentAccount.getBalance());
                balanceLabel.setText(balance_info);

                List<Transaction> history = manager.findTransactionsBySender(currentAccount.getAccountId());
                transactionHistoryModel = new TransactionsHistoryModel(history);
                transactionHistoryTable.setModel(transactionHistoryModel);
            } catch (SQLException e){
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }

    public void  handleModifyButton() {
        ModifyProfilePanel modifyPanel = (ModifyProfilePanel) SwingUtilities.findPanelByName(cardPanel, "ModifyPanel");
        if (modifyPanel != null){
            modifyPanel.setUser(user);
            cardLayout.show(cardPanel, "ModifyProfile");
        }

    }

    public void handleTransactionsButton() throws SQLException {
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
        savingsPanel.setUser(user);
        cardLayout.show(cardPanel, "StandingOrders");
    }
}
