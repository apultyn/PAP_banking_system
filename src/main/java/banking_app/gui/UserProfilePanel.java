package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.Transaction;
import banking_app.classes.User;
import banking_exceptions.*;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
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
    private JLabel transactionLimitLabel;
    private JButton modifyLimitButton;
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

        transactionHistoryModel = new TransactionsHistoryModel(new ArrayList<>(), 0L);
        transactionHistoryTable = new JTable(transactionHistoryModel);
        JScrollPane scrollPane = new JScrollPane(transactionHistoryTable);

        menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton transactionsButton = new JButton("Transactions");
        JButton contactsButton = new JButton("Contacts");
        JButton createAccountButton = new JButton("Create Account");
        JButton modifyProfileButton = new JButton("Modify data");
        JButton automaticSavingButton = new JButton("Automatic Savings");
        JButton accountsButton = new JButton("My Accounts");
        JButton standingOrdersButton = new JButton("Standing Orders");

        menuPanel.add(transactionsButton);
        menuPanel.add(contactsButton);
        menuPanel.add(createAccountButton);
        menuPanel.add(modifyProfileButton);
        menuPanel.add(automaticSavingButton);
        menuPanel.add(accountsButton);
        menuPanel.add(standingOrdersButton);

        modifyProfileButton.addActionListener(e -> handleModifyButton());
        automaticSavingButton.addActionListener(e -> handleAutomaticSavings());
        accountsButton.addActionListener(e -> handleAccountsButton());
        standingOrdersButton.addActionListener(e->handleStandingOrders());
        createAccountButton.addActionListener(e->handleCreateAccountButton());
        contactsButton.addActionListener(e->handleContactsButton());
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
        transactionLimitLabel = new JLabel();

        modifyLimitButton = new JButton("Change transactions limit");

        JPanel limitsPanel = new JPanel();
        limitsPanel.setLayout(new BoxLayout(limitsPanel, BoxLayout.X_AXIS));
        limitsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Dodanie marginesów

        Dimension labelMaxSize = new Dimension(350, 50);
        transactionLimitLabel.setMaximumSize(labelMaxSize);
        transactionLimitLabel.setPreferredSize(labelMaxSize);

        Dimension buttonMaxSize = new Dimension(250, 50);
        modifyLimitButton.setMaximumSize(buttonMaxSize);

        limitsPanel.add(Box.createHorizontalGlue()); // Elastyczna przestrzeń po lewej stronie
        limitsPanel.add(transactionLimitLabel);
        limitsPanel.add(modifyLimitButton);
        limitsPanel.add(Box.createHorizontalGlue()); // Elastyczna przestrzeń po lewej stronie

        modifyLimitButton.addActionListener(e -> openModifyDialog());

        helloLabel.setFont(helloLabel.getFont().deriveFont(24f));
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(20f));

        accountComboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        accountComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        contentPanel.add(Box.createHorizontalGlue());
        contentPanel.add(helloLabel);
        contentPanel.add(Box.createHorizontalGlue());
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(accountComboBox);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(accountDetailsLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(balanceLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(limitsPanel);

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

    private void openModifyDialog() {
        Dialog dialog = new JDialog();
        dialog.setSize(350, 250);
        dialog.setLayout(new GridLayout(0, 2));
        ((JComponent) ((JDialog) dialog).getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        dialog.add(new JLabel("New transaction limit:"));
        JTextField newLimitField = new JTextField();
        dialog.add(newLimitField);

        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        dialog.add(confirmButton);
        dialog.add(cancelButton);

        confirmButton.addActionListener(e -> {
            try {
                currentAccount.updateTransactionLimit(manager, newLimitField.getText());
                JOptionPane.showMessageDialog(dialog, "Modification of transaction limit successful.");
                setUser(manager.findUser(user.getEmail()));
                dialog.dispose();
            } catch (InvalidAmountException exception) {
                JOptionPane.showMessageDialog(dialog, exception.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database error!");
            }
        });

        // Cancel button action
        cancelButton.addActionListener(e -> dialog.dispose());

        //dialog.pack();
        dialog.setLocationRelativeTo(SwingUtilities.findPanelByName(cardPanel, "ModifyPanel"));
        dialog.setVisible(true);
    }

    public void setAccounts(List<Account> accounts) {
        accountComboBox.removeAllItems();
        for (Account account : accounts) {
            String item = account.getName() + " - " + account.getAccountId();
            accountComboBox.addItem(item);
        }
    }

    private void updateAccountInfo(long accountId) throws SQLException {
        currentAccount = manager.findAccount(accountId);
        String account_info = "Choosen account: " + currentAccount.getName() + ", Account ID: " + currentAccount.getAccountId()
                + "\n Created: " + currentAccount.getDateCreated();
        accountDetailsLabel.setText(account_info);

        String balance_info = String.format("Balance: %.2f zł", currentAccount.getBalance());
        balanceLabel.setText(balance_info);

        String limit_info = String.format("Your limit: %.2f zł", currentAccount.getTransactionLimit());
        transactionLimitLabel.setText(limit_info);
    }

    private void createTranscationsHistory() throws SQLException {
        List<Transaction> incomingTransactions = manager.findTransactionsByReceiver(currentAccount.getAccountId());
        List<Transaction> outgoingTransactions = manager.findTransactionsBySender(currentAccount.getAccountId());
        List<Transaction> allTransactions = new ArrayList<>(incomingTransactions);
        allTransactions.addAll(outgoingTransactions);

        allTransactions.sort(Comparator.comparing(Transaction::getDate).reversed());

        transactionHistoryModel = new TransactionsHistoryModel(allTransactions, currentAccount.getAccountId());
        transactionHistoryTable.setModel(transactionHistoryModel);

    }

    private void onAccountSelected() {
        String selected = (String) accountComboBox.getSelectedItem();
        if (selected != null) {
            String[] parts = selected.split(" - ");
            String accountName = parts[0];
            String accountId = parts.length > 1 ? parts[1] : "";

            try {
                updateAccountInfo(Long.parseLong(accountId));
                createTranscationsHistory();
            } catch (SQLException e){
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }

    public void  handleModifyButton() {
        ModifyProfilePanel modifyPanel = (ModifyProfilePanel) SwingUtilities.findPanelByName(cardPanel, "ModifyPanel");
        if (modifyPanel != null){
            modifyPanel.setUser(user);
            cardLayout.show(cardPanel, "ModifyPanel");
        }
    }

    private void handleCreateAccountButton() {
        CreateAccountPanel createAccountPanel = (CreateAccountPanel) SwingUtilities.findPanelByName(cardPanel, "CreateAccount");
        if (createAccountPanel != null){
            createAccountPanel.setUser(user);
            cardLayout.show(cardPanel, "CreateAccount");
        }
    }

    private void handleContactsButton() {
        ContactsPanel contactsPanel = (ContactsPanel) SwingUtilities.findPanelByName(cardPanel, "Contacts");
        if (contactsPanel != null) {
            contactsPanel.setUser(user);
            cardLayout.show(cardPanel, "Contacts");
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
