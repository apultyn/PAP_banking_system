package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.AutomaticSaving;
import banking_app.classes.User;
import banking_exceptions.InvalidAccountNumberException;
import banking_exceptions.InvalidAmountException;
import banking_exceptions.InvalidNameException;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AutomaticSavingsGui extends JPanel {
    private User user;
    private final ConnectionManager manager;
    private final JPanel cardPanel;
    private final JPanel detailsPanel;
    private final JButton backButton;
    private final JButton registerSavings;
    private final JList<String> asList;
    private final DefaultListModel<String> listModel;
    private final JTextArea AutomaticSDetails;
    private final JLabel nameLabel;
    private final JLabel startDateLabel;
    private final JLabel senderIdLabel;
    private final JLabel receiverIdLabel;
    private final JLabel amountLabel;
    private JComboBox<String> accountComboBox, receiverComboBox;
    private JTextField nameField, amountField;
    Map<String, AutomaticSaving> asMap = new HashMap<>();
    public AutomaticSavingsGui(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.manager = manager;
        this.cardPanel = cardPanel;
        this.setName(panelName);

        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        asList = new JList<>(listModel);
        add(new JScrollPane(asList), BorderLayout.WEST);

        AutomaticSDetails = new JTextArea(10, 30);
        AutomaticSDetails.setEditable(false);
        add(AutomaticSDetails, BorderLayout.CENTER);

        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        nameLabel = new JLabel("Name:");
        startDateLabel = new JLabel("Date started:");
        senderIdLabel = new JLabel("From which account:");
        receiverIdLabel = new JLabel("To which account:");
        amountLabel = new JLabel("How much do you want to send:");

        detailsPanel.add(nameLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(startDateLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(senderIdLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(receiverIdLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(amountLabel);
        detailsPanel.setVisible(false);
        add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Back");
        registerSavings = new JButton("Create New Automatic Saving");
        buttonPanel.add(backButton);
        buttonPanel.add(registerSavings);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e-> {
            UserProfilePanel userPanel = (UserProfilePanel) SwingUtilities.findPanelByName(cardPanel,"User");
            if (userPanel != null && user != null){
                userPanel.setUser(user);
            }
            cardLayout.show(cardPanel, "User");
        });

        asList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedName = asList.getSelectedValue();
                AutomaticSaving selectedAS = asMap.get(selectedName);
                displayASDetails(selectedAS);
            }
        });

        registerSavings.addActionListener(e->{
            try {
                createNewStandingOrder();
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        try {
            updateASList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createNewStandingOrder() throws SQLException {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create Automatic Saving");
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(0, 2));

        ArrayList<Account> accounts = new ArrayList<>(manager.findUsersAccounts(user.getId()));

        nameField = new JTextField();
        amountField = new JTextField();
        receiverComboBox = new JComboBox<>();
        accountComboBox = new JComboBox<>();

        for (Account account : accounts) {
            accountComboBox.addItem(account.getName());
            receiverComboBox.addItem(account.getName());
        }

        dialog.add(new JLabel("Saving Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Amount:"));
        dialog.add(amountField);
        dialog.add(new JLabel("Recipient:"));
        dialog.add(receiverComboBox);
        dialog.add(new JLabel("Sender:"));
        dialog.add(accountComboBox);

        JButton submitButton = new JButton("Create");
        submitButton.addActionListener(e->handleCreateAutomaticSaving(accounts, dialog));
        dialog.add(submitButton);
        JButton goBackButton = new JButton("Back");
        goBackButton.addActionListener(e -> {
            dialog.dispose();
        });
        dialog.add(goBackButton);

        dialog.setLocationRelativeTo(SwingUtilities.findPanelByName(cardPanel, "SavingsPanel"));
        dialog.setVisible(true);
    }

    private boolean validatePIN(String PIN){
        return PIN.equals(user.getPin());
    }

    private void handleCreateAutomaticSaving (ArrayList<Account> accounts, JDialog dialog) {
        Account selectedSender = getSelectedAccount(accountComboBox, accounts);
        Account selectedRecipient = getSelectedAccount(receiverComboBox, accounts);
        try {
            AutomaticSaving automaticSaving = new AutomaticSaving(nameField.getText(), amountField.getText(), String.valueOf(selectedSender.getAccountId()),
                    String.valueOf(selectedRecipient.getAccountId()));
            int attempts = 3;
            while (attempts > 0) {
                String PIN = JOptionPane.showInputDialog(this, "Enter your PIN: (Attempts left: " + attempts + ")");
                if (PIN == null)
                    break;
                else if (!validatePIN(PIN)) {
                    attempts--;
                    JOptionPane.showMessageDialog(this, "Incorrect PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    AutomaticSaving.registerAutomaticSaving(manager, automaticSaving);
                    JOptionPane.showMessageDialog(this, "Created successfully!");
                    dialog.dispose();
                    break;
                }
            }
            updateASList();
        } catch (NumberFormatException | InvalidNameException | InvalidAmountException |
                 InvalidAccountNumberException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Account getSelectedAccount(JComboBox<String> comboBox, ArrayList<Account> accounts) {
        int selectedIndex = comboBox.getSelectedIndex();
        return selectedIndex >= 0 ? accounts.get(selectedIndex) : null;
    }

    private void updateASList() throws SQLException {
        if (user != null) {
            listModel.clear();
            asMap.clear();
            ArrayList<AutomaticSaving> automaticSavings = new ArrayList<>(manager.findUsersSavings(user.getId()));
            for (AutomaticSaving saving : automaticSavings) {
                listModel.addElement(saving.getName());
                asMap.put(saving.getName(), saving);
            }
        }
    }
    private void displayASDetails(AutomaticSaving selectedAS) {
        if (selectedAS != null) {
            nameLabel.setText("Name: " + selectedAS.getName());
            startDateLabel.setText("Date started: " + selectedAS.getDateStarted());
            senderIdLabel.setText("From which account: " + selectedAS.getSourceAccountId());
            receiverIdLabel.setText("To which account: " + selectedAS.getTargetAccountId());
            amountLabel.setText("How much do you want to send: " + selectedAS.getAmount().toString());
            detailsPanel.setVisible(true);
        } else
            detailsPanel.setVisible(false);
    }

    public void setUser (User user) {
        this.user = user;
        try {
            updateASList();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "BD fault", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
