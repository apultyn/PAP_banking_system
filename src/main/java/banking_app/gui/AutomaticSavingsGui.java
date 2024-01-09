package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.AutomaticSaving;
import banking_app.classes.StandingOrder;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AutomaticSavingsGui extends JPanel {
    private User user;
    private ArrayList<StandingOrder> orders;

    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel, detailsPanel;
    private JButton backButton;
    private JButton registerSavings;
    private JList<String> asList;
    private DefaultListModel<String> listModel;
    private JTextArea AutomaticSDetails;

    private JLabel nameLabel, startDateLabel, senderIdLabel, recieverIdLabel, amountLabel;
    Map<String, AutomaticSaving> asMap = new HashMap<>();
    public AutomaticSavingsGui(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.manager = manager;
        this.cardLayout = cardLayout;
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
        recieverIdLabel = new JLabel("To which account:");
        amountLabel = new JLabel("How much do you want to send:");

        detailsPanel.add(nameLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(startDateLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(senderIdLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(recieverIdLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(amountLabel);

        add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Go Back");
        registerSavings = new JButton("Create New Automatic Saving");
        buttonPanel.add(backButton);
        buttonPanel.add(registerSavings);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e-> cardLayout.show(cardPanel, "User"));

        asList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedName = asList.getSelectedValue();
                AutomaticSaving selectedAS = asMap.get(selectedName);
                displayASDetails(selectedAS);
            }
        });

        registerSavings.addActionListener(e->{
            try {
                createNewStandingOrder(listModel);
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        try {
            updateASList();  // Refresh the deposit list every time the panel is shown
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the SQLException appropriately
        }
    }

    private void createNewStandingOrder(DefaultListModel<String> listModel) throws SQLException {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create Automatic Saving");
        dialog.setSize(400, 300); // Set the size of the dialog
        dialog.setLayout(new GridLayout(0, 2)); // Using GridLayout for simplicity

        ArrayList<Account> accounts = new ArrayList<>(manager.findUsersAccounts(user.getId()));

        JTextField nameField = new JTextField();
        JTextField amountField = new JTextField();
        JComboBox<String> recieverComboBox = new JComboBox<>();
        JComboBox<String> accountComboBox = new JComboBox<>();

        // Populate accountComboBox with account names
        for (Account account : accounts) {
            accountComboBox.addItem(account.getName());
            recieverComboBox.addItem(account.getName());
        }

        // Adding form fields to the dialog
        dialog.add(new JLabel("Saving Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Amount:"));
        dialog.add(amountField);
        dialog.add(new JLabel("Recipient:"));
        dialog.add(recieverComboBox);
        dialog.add(new JLabel("Account:"));
        dialog.add(accountComboBox);

        // Add a submit button
        JButton submitButton = new JButton("Create");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account selectedAccount = getSelectedAccount(accountComboBox, accounts);
                Account selectedRecipient = getSelectedAccount(recieverComboBox, accounts);
                try {
                    String name = nameField.getText();
                    String amount = amountField.getText();
                    AutomaticSaving.registerAutomaticSaving(manager, user, name,
                            String.valueOf(selectedAccount.getAccountId()),
                            String.valueOf(selectedRecipient.getAccountId()), amount);
                    JOptionPane.showMessageDialog(dialog, "Automatic Saving created");
                    dialog.dispose(); // Close the creating deposit window
                    updateASList(); // Update deposit list in main panel
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Wrong input!");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        dialog.add(submitButton);
        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Close the dialog
            }
        });
        dialog.add(goBackButton);

        // Display the dialog
        dialog.setVisible(true);
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
            recieverIdLabel.setText("To which account: " + selectedAS.getTargetAccountId());
            amountLabel.setText("How much do you want to send: " + selectedAS.getAmount().toString());
        }
    }

    public void setUser (User user) {
        this.user = user;
        try {
            updateASList();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "BD fault");
        }
    }
}
