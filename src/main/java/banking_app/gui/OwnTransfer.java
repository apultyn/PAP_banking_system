package banking_app.gui;

import banking_app.classes.Account;
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

public class OwnTransfer extends JPanel {
    private User user;
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JLabel transferLabel;
    private JLabel balanceLabel;
    private JTextField titleField, amountField;
    private JComboBox<Long> senderComboBox;
    private JComboBox<Long> recipientComboBox;
    private JButton transferButton, backButton;

    public OwnTransfer(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.setName(panelName);
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        this.manager = manager;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        add(transferLabel = new JLabel("New own transfer"), gbc);
        transferLabel.setFont(new Font(transferLabel.getFont().getFontName(), Font.BOLD, 24));
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        addLabelAndComponent(this, "Title:", titleField = new JTextField(20), gbc);
        addLabelAndComponent(this,"From account:", senderComboBox = new JComboBox<>(), gbc);
        addLabelAndComponent(this, "Balance:", balanceLabel = new JLabel(), gbc);
        addLabelAndComponent(this, "To account:", recipientComboBox = new JComboBox<>(), gbc);

        addLabelAndComponent(this, "Amount:", amountField = new JTextField(20), gbc);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        transferButton = new JButton("Transfer");
        backButton = new JButton("Back");
        senderComboBox.addActionListener(e -> {
            if (senderComboBox.getSelectedItem() != null) {
                try {
                    balanceLabel.setText(String.format("%.2f pln", manager.findAccount(Long.parseLong(Objects.requireNonNull(senderComboBox.getSelectedItem()).toString())).getBalance()));
                    balanceLabel.setFont(new Font(balanceLabel.getFont().getFontName(), Font.BOLD, balanceLabel.getFont().getSize()));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        transferButton.addActionListener(e -> handleMakeTransfer());
        backButton.addActionListener(e -> {
            resetComponents(this);
            cardLayout.show(cardPanel, "User");
        });
        add(transferButton, gbc);
        gbc.gridy++;
        add(backButton, gbc);

    }

    private void handleMakeTransfer() {
        try {
            Transaction transaction = new Transaction(user.getName() + user.getSurname(), recipientComboBox.getSelectedItem().toString(),
                    senderComboBox.getSelectedItem().toString(), titleField.getText(), amountField.getText());
            user.makeTransaction(manager, transaction);
            JOptionPane.showMessageDialog(this, String.format("Transferred successfully!\nYour balance is now %.2f pln", manager.findAccount(Long.parseLong(senderComboBox.getSelectedItem().toString())).getBalance()) );
            cardLayout.show(cardPanel, "User");
            resetComponents(this);
        } catch (InvalidAccountNumberException | InvalidNameException | InvalidAmountException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    private void updateSenderAccountList() throws SQLException, NullPointerException {
        List<Account> accounts = manager.findUsersAccounts(user.getId());
        //senderComboBox.removeAllItems();
        for (var account : accounts) {
//            if (recipientComboBox.getSelectedItem() != null) {
//                if (account.getAccountId() == Long.parseLong(recipientComboBox.getSelectedItem().toString())) {
//                    continue;
//                }
//            }
            senderComboBox.addItem(account.getAccountId());
        }
    }

    private void updateRecipientAccountList() throws SQLException, NullPointerException {
        List<Account> accounts = manager.findUsersAccounts(user.getId());
        //recipientComboBox.removeAllItems();
        for (var account : accounts) {
//            if (senderComboBox.getSelectedItem() != null) {
//                if (account.getAccountId() == Long.parseLong(senderComboBox.getSelectedItem().toString())) {
//                    continue;
//                }
//            }
            recipientComboBox.addItem(account.getAccountId());
        }
    }

    private boolean validatePIN(String PIN) {
        return PIN.equals(user.getPin());
    }

    public void setUser(User user) {
        this.user = user;
        try {
            updateRecipientAccountList();
            updateSenderAccountList();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}
