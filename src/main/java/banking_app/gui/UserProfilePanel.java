package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.Transfer;
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
    private final JLabel helloLabel;
    private final ConnectionManager manager;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JComboBox<String> accountComboBox;
    private Account currentAccount;
    private final JLabel accountDetailsLabel;
    private final JLabel balanceLabel;
    private final JLabel transferLimitLabel;
    private final JButton modifyLimitButton;
    private final JPanel menuPanel;
    private final JPanel contentPanel;

    private final JTable transferHistoryTable;
    private TransfersHistoryModel transferHistoryModel;

    public UserProfilePanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.setName(panelName);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.manager = manager;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        transferHistoryModel = new TransfersHistoryModel(new ArrayList<>(), 0L);
        transferHistoryTable = new JTable(transferHistoryModel);
        JScrollPane scrollPane = new JScrollPane(transferHistoryTable);

        menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton transfersButton = new JButton("Transfers");
        JButton contactsButton = new JButton("Contacts");
        JButton createAccountButton = new JButton("Create Account");
        JButton modifyProfileButton = new JButton("Modify data");
        JButton automaticSavingButton = new JButton("Automatic Savings");
        JButton depositsButton = new JButton("Deposits");
        JButton standingOrdersButton = new JButton("Standing Orders");
        JButton loanCalculatorButton = new JButton("Loan Calculator");
        JButton loanButton = new JButton("Loans");
        JButton ownTransferButton = new JButton("Own Transfer");
        JButton logOutButton = new JButton("LogOut");

        menuPanel.add(transfersButton);
        menuPanel.add(ownTransferButton);
        menuPanel.add(depositsButton);
        menuPanel.add(contactsButton);
        menuPanel.add(createAccountButton);
        menuPanel.add(modifyProfileButton);
        menuPanel.add(automaticSavingButton);
        menuPanel.add(depositsButton);
        menuPanel.add(standingOrdersButton);
        menuPanel.add(loanCalculatorButton);
        menuPanel.add(loanButton);
        menuPanel.add(logOutButton);

        modifyProfileButton.addActionListener(e -> handleModifyButton());
        automaticSavingButton.addActionListener(e -> handleAutomaticSavings());
        depositsButton.addActionListener(e -> {
            try {
                handleDepositsButton();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        standingOrdersButton.addActionListener(e->handleStandingOrders());
        loanCalculatorButton.addActionListener(e->handleLoanCalculator());
        loanButton.addActionListener(e->handleLoansButton());

        createAccountButton.addActionListener(e->handleCreateAccountButton());
        contactsButton.addActionListener(e->handleContactsButton());
        ownTransferButton.addActionListener(e->handleOwnTransferButton());
        transfersButton.addActionListener(e -> {
            try {
                handleTransfersButton();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        logOutButton.addActionListener(e -> {
            user = null;
            JOptionPane.showMessageDialog(cardPanel, "Logged out!");
            cardLayout.show(cardPanel, "Login");
        });

        setLayout(new BorderLayout());
        add(menuPanel, BorderLayout.NORTH);

        helloLabel = new JLabel();
        accountComboBox = new JComboBox<>();
        accountDetailsLabel = new JLabel();
        balanceLabel = new JLabel();
        transferLimitLabel = new JLabel();

        modifyLimitButton = new JButton("Change transfers limit");

        JPanel limitsPanel = new JPanel();
        limitsPanel.setLayout(new BoxLayout(limitsPanel, BoxLayout.X_AXIS));
        limitsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        Dimension labelMaxSize = new Dimension(350, 50);
        transferLimitLabel.setMaximumSize(labelMaxSize);
        transferLimitLabel.setPreferredSize(labelMaxSize);

        Dimension buttonMaxSize = new Dimension(250, 50);
        modifyLimitButton.setMaximumSize(buttonMaxSize);

        limitsPanel.add(Box.createHorizontalGlue());
        limitsPanel.add(transferLimitLabel);
        limitsPanel.add(modifyLimitButton);
        limitsPanel.add(Box.createHorizontalGlue());

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
            createTransfersHistory();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, e);
        }

    }

    private void openModifyDialog() {
        Dialog dialog = new JDialog();
        dialog.setSize(350, 250);
        dialog.setLayout(new GridLayout(0, 2));
        ((JComponent) ((JDialog) dialog).getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        dialog.add(new JLabel("New transfer limit:"));
        JTextField newLimitField = new JTextField();
        dialog.add(newLimitField);

        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        dialog.add(confirmButton);
        dialog.add(cancelButton);

        confirmButton.addActionListener(e -> {
            try {
                currentAccount.updateTransferLimit(manager, newLimitField.getText());
                JOptionPane.showMessageDialog(dialog, "Modification of transfer limit successful.");
                setUser(manager.findUser(user.getEmail()));
                dialog.dispose();
            } catch (InvalidAmountException exception) {
                JOptionPane.showMessageDialog(dialog, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
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

        String balance_info = String.format("Balance: %.2f pln", currentAccount.getBalance());
        balanceLabel.setText(balance_info);

        String limit_info = String.format("Your limit: %.2f pln", currentAccount.getTransferLimit());
        transferLimitLabel.setText(limit_info);
    }

    public void createTransfersHistory() throws SQLException {
        List<Transfer> incomingTransfers = manager.findTransfersByReceiver(currentAccount.getAccountId());
        List<Transfer> outgoingTransfers = manager.findTransfersBySender(currentAccount.getAccountId());
        List<Transfer> allTransfers = new ArrayList<>(incomingTransfers);
        allTransfers.addAll(outgoingTransfers);

        allTransfers.sort(Comparator.comparing(Transfer::getDate).reversed());

        transferHistoryModel = new TransfersHistoryModel(allTransfers, currentAccount.getAccountId());
        transferHistoryTable.setModel(transferHistoryModel);

    }

    private void onAccountSelected() {
        String selected = (String) accountComboBox.getSelectedItem();
        if (selected != null) {
            String[] parts = selected.split(" - ");
            String accountId = parts.length > 1 ? parts[1] : "";

            try {
                updateAccountInfo(Long.parseLong(accountId));
                createTransfersHistory();
            } catch (SQLException e){
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }

    public void  handleModifyButton() {
        ModifyProfilePanel modifyPanel = (ModifyProfilePanel) SwingUtilities.findPanelByName(cardPanel, "ModifyPanel");
        if (modifyPanel != null) {
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

    private void handleOwnTransferButton() {
        OwnTransfersPanel ownTransfersPanel = (OwnTransfersPanel) SwingUtilities.findPanelByName(cardPanel, "OwnTransfer");
        if (ownTransfersPanel != null) {
            ownTransfersPanel.setUser(user);
            cardLayout.show(cardPanel, "OwnTransfer");
        }
    }

    public void handleTransfersButton() throws SQLException {
        TransfersPanel transfersPanel = (TransfersPanel) SwingUtilities.findPanelByName(cardPanel, "Transfers");
        if (transfersPanel != null) {
            transfersPanel.setUser(user);
            cardLayout.show(cardPanel, "Transfers");
        }
    }

    public void handleAutomaticSavings() {
        AutomaticSavingsGui savingsPanel = (AutomaticSavingsGui) SwingUtilities.findPanelByName(cardPanel, "SavingsPanel");
        if (savingsPanel != null) {
            savingsPanel.setUser(user);
            cardLayout.show(cardPanel, "SavingsPanel");
        }
    }

    public void handleDepositsButton() throws SQLException {
        DepositPanel depositPanel = (DepositPanel) SwingUtilities.findPanelByName(cardPanel, "Deposit");
        if (depositPanel != null) {
            depositPanel.setUser(user);
            cardLayout.show(cardPanel, "Deposit");
        }
    }
    public void handleStandingOrders() {
        StandingOrdersPanel savingsPanel = (StandingOrdersPanel) SwingUtilities.findPanelByName(cardPanel, "StandingOrders");
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
