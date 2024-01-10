package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.EmailSender;
import banking_app.classes.Transaction;
import banking_app.classes.User;
import banking_exceptions.InvalidAccountNumberException;
import banking_exceptions.InvalidAmountException;
import banking_exceptions.InvalidNameException;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static banking_app.gui.SwingUtilities.addLabelAndComponent;
import static banking_app.gui.SwingUtilities.resetComponents;

public class TransactionsPanel extends JPanel {
    private User user;
    private ConnectionManager manager;
    private JLabel transferLabel;
    private JLabel balanceLabel;
    private JTextField recipientNameField, recipientNumberField, titleField;
    protected JComboBox<Long> accountComboBox;
    private JTextField amountField;
    private JButton transferButton;
    private JButton backButton;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    public TransactionsPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) throws SQLException {
        this.setName(panelName);
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        add(transferLabel = new JLabel("New transfer"), gbc);
        transferLabel.setFont(transferLabel.getFont().deriveFont(Font.BOLD, 24));
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        addLabelAndComponent(this, "Recipient name:", recipientNameField = new JTextField(20), gbc);
        addLabelAndComponent(this, "Recipient account number:", recipientNumberField = new JTextField(20), gbc);
        addLabelAndComponent(this, "Title:", titleField = new JTextField(20), gbc);
        addLabelAndComponent(this, "From account:", accountComboBox = new JComboBox<>(), gbc);
        addLabelAndComponent(this, "Balance:", balanceLabel = new JLabel(), gbc);
        addLabelAndComponent(this, "Amount:", amountField = new JTextField(20), gbc);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        transferButton = new JButton("Transfer");
        backButton = new JButton("Back");

        accountComboBox.addActionListener(e -> {
            if (accountComboBox.getSelectedItem() != null) {
                try {
                    balanceLabel.setText(String.format("%.2f pln", manager.findAccount(Long.parseLong(Objects.requireNonNull(accountComboBox.getSelectedItem()).toString())).getBalance()));
                    balanceLabel.setFont(balanceLabel.getFont().deriveFont(Font.BOLD));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        transferButton.addActionListener(e -> handleMakeTransfer());
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "User");
            balanceLabel.setText("");
            resetComponents(this);
        });
        add(transferButton, gbc);
        gbc.gridy++;
        add(backButton, gbc);
    }

    public void setRecipientName(String name) {
        recipientNameField.setText(name);
    }

    public void setRecipientNumber(long accountId) {
        recipientNumberField.setText(String.valueOf(accountId));
    }

    private void handleMakeTransfer() {
        try {
            Transaction transaction = new Transaction(recipientNameField.getText(), recipientNumberField.getText(), Objects.requireNonNull(accountComboBox.getSelectedItem().toString()), titleField.getText(), amountField.getText());

            int attempts = 3;
            while (attempts > 0) {
                String PIN = JOptionPane.showInputDialog(this, "Enter your PIN: (Attempts left: " + attempts + ")");
                if (PIN == null)
                    break;
                else if (!validatePIN(PIN)) {
                    attempts--;
                    JOptionPane.showMessageDialog(this, "Incorrect PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    user.makeTransaction(manager, transaction);
                    JOptionPane.showMessageDialog(this, String.format("Transferred successfully!\nYour balance is now %.2f pln", manager.findAccount(Long.parseLong(accountComboBox.getSelectedItem().toString())).getBalance()) );
                    new EmailSender(manager).sendTransactionInfo(transaction);
                    break;
                }
            }
            ((UserProfilePanel) Objects.requireNonNull(SwingUtilities.findPanelByName(cardPanel, "User"))).createTranscationsHistory();
            cardLayout.show(cardPanel, "User");
            balanceLabel.setText("");
            resetComponents(this);
        } catch (InvalidAccountNumberException | InvalidNameException | InvalidAmountException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAccountList() throws SQLException, NullPointerException {
        List<Account> accounts = manager.findUsersAccounts(user.getId());
        for (var account : accounts) {
            accountComboBox.addItem(account.getAccountId());
        }
    }

    private boolean validatePIN(String PIN) {
        return PIN.equals(user.getPin());
    }

    public void setUser(User user) throws SQLException {
        this.user = user;
        updateAccountList();
        if (accountComboBox.getSelectedItem() != null) {
            balanceLabel.setText(String.format("%.2f pln", manager.findAccount(Long.parseLong(Objects.requireNonNull(accountComboBox.getSelectedItem()).toString())).getBalance()));
        }
    }
}
