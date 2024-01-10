package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.Transfer;
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

public class OwnTransfersPanel extends JPanel {
    private User user;
    private final ConnectionManager manager;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JLabel transferLabel;
    private final JLabel balanceLabel;
    private final JTextField titleField;
    private final JTextField amountField;
    private final JComboBox<Long> senderComboBox;
    private final JComboBox<Long> recipientComboBox;
    private final JButton transferButton;
    private final JButton backButton;

    public OwnTransfersPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
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
            UserProfilePanel userPanel = (UserProfilePanel) SwingUtilities.findPanelByName(cardPanel,"User");
            if (userPanel != null && user != null){
                userPanel.setUser(user);
            }
            cardLayout.show(cardPanel, "User");
        });
        add(transferButton, gbc);
        gbc.gridy++;
        add(backButton, gbc);

    }

    private void handleMakeTransfer() {
        try {
            Transfer transfer = new Transfer(user.getName() + user.getSurname(), recipientComboBox.getSelectedItem().toString(),
                    senderComboBox.getSelectedItem().toString(), titleField.getText(), amountField.getText());
            user.makeTransfer(manager, transfer);
            JOptionPane.showMessageDialog(this, String.format("Transferred successfully!\nYour balance is now %.2f pln", manager.findAccount(Long.parseLong(senderComboBox.getSelectedItem().toString())).getBalance()) );
            UserProfilePanel userPanel = (UserProfilePanel) SwingUtilities.findPanelByName(cardPanel,"User");
            if (userPanel != null && user != null){
                userPanel.setUser(user);
            }
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
        for (var account : accounts) {
            senderComboBox.addItem(account.getAccountId());
        }
    }

    private void updateRecipientAccountList() throws SQLException, NullPointerException {
        List<Account> accounts = manager.findUsersAccounts(user.getId());
        //recipientComboBox.removeAllItems();
        for (var account : accounts) {
            recipientComboBox.addItem(account.getAccountId());
        }
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
