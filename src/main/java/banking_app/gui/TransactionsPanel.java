package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.User;
import banking_exceptions.InvalidAccountNumberException;
import banking_exceptions.InvalidAmountException;
import banking_exceptions.InvalidNameException;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import static banking_app.gui.SwingUtilities.resetComponents;

public class TransactionsPanel extends JPanel {
    private User user;
    private ConnectionManager manager;
    private JLabel transferLabel;
    private JLabel balanceLabel;
    private JTextField recipientNameField;
    private JTextField recipientNumberField;
    private JTextField titleField;
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
        transferLabel.setFont(new Font(transferLabel.getFont().getFontName(), Font.BOLD, 24));
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        addLabelAndComponent("Recipient name:", recipientNameField = new JTextField(20), gbc);
        addLabelAndComponent("Recipient account number:", recipientNumberField = new JTextField(20), gbc);
        addLabelAndComponent("Title:", titleField = new JTextField(20), gbc);
        addLabelAndComponent("From account:", accountComboBox = new JComboBox<>(), gbc);
        addLabelAndComponent("Amount:", amountField = new JTextField(20), gbc);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        transferButton = new JButton("Transfer");
        backButton = new JButton("Back");
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
            user.makeTransaction(manager, recipientNameField.getText(), recipientNumberField.getText(),
                    accountComboBox.getSelectedItem().toString(), titleField.getText(), amountField.getText());
            JOptionPane.showMessageDialog(this, String.format("Transferred successfully!\nYour balance is now %.2f pln", manager.findAccount(Long.parseLong(accountComboBox.getSelectedItem().toString())).getBalance()) );
            cardLayout.show(cardPanel, "User");
            resetComponents(this);
        } catch (InvalidAccountNumberException | InvalidNameException | InvalidAmountException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void addLabelAndComponent(String labelText, Component component, GridBagConstraints gbc) {
        add(new JLabel(labelText), gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(component, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private void updateAccountList() throws SQLException, NullPointerException {
        List<Account> accounts = manager.findUsersAccounts(user.getId());
        for (var account : accounts) {
            accountComboBox.addItem(account.getAccountId());
        }
    }

    public void setUser(User user) throws SQLException {
        this.user = user;
        updateAccountList();
    }
}
