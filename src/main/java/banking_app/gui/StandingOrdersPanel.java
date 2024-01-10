package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.StandingOrder;
import banking_app.classes.User;
import banking_exceptions.InvalidAccountNumberException;
import banking_exceptions.InvalidAmountException;
import banking_exceptions.InvalidNameException;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StandingOrdersPanel extends JPanel {
    private User user;
    private ArrayList<StandingOrder> orders;

    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel, detailsPanel;
    private JTextField nameField , amountField, recipientField;
    private JComboBox<String> accountComboBox;
    private JButton backButton, registerOrder;
    private JList<String> asList;
    private DefaultListModel<String> listModel;
    private JTextArea standingODetails;

    private JLabel nameLabel, startDateLabel, senderIdLabel, recipientIdLabel, amountLabel;
    Map<String, StandingOrder> soMap = new HashMap<>();
    public StandingOrdersPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.setName(panelName);

        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        asList = new JList<>(listModel);
        add(new JScrollPane(asList), BorderLayout.WEST);

        standingODetails = new JTextArea(10, 30);
        standingODetails.setEditable(false);
        add(standingODetails, BorderLayout.CENTER);


        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        nameLabel = new JLabel("Name:");
        startDateLabel = new JLabel("Date started:");
        senderIdLabel = new JLabel("From which account:");
        recipientIdLabel = new JLabel("To which account:");
        amountLabel = new JLabel("How much:");

        detailsPanel.add(nameLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(startDateLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(senderIdLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(recipientIdLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(amountLabel);

        add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Go Back");
        registerOrder = new JButton("Create New Standing Order");
        buttonPanel.add(backButton);
        buttonPanel.add(registerOrder);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e-> cardLayout.show(cardPanel, "User"));

        asList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedName = asList.getSelectedValue();
                StandingOrder selectedSO = soMap.get(selectedName);
                displaySODetails(selectedSO);
            }
        });

        registerOrder.addActionListener(e-> {
            try {
                createNewStandingOrder(listModel);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        try {
            updateSOList();  // Refresh the deposit list every time the panel is shown
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the SQLException appropriately
        }
    }

    private void createNewStandingOrder(DefaultListModel<String> listModel) throws SQLException {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create New Standing order");
        dialog.setSize(400, 300); // Set the size of the dialog
        dialog.setLayout(new GridLayout(0, 2)); // Using GridLayout for simplicity

        ArrayList<Account> accounts = new ArrayList<>(manager.findUsersAccounts(user.getId()));

        nameField = new JTextField();
        amountField = new JTextField();
        recipientField = new JTextField();
        accountComboBox = new JComboBox<>();

        // Populate accountComboBox with account names
        for (Account account : accounts) {
            accountComboBox.addItem(account.getName());
        }

        // Adding form fields to the dialog
        dialog.add(new JLabel("Order Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Amount:"));
        dialog.add(amountField);
        dialog.add(new JLabel("Recipient:"));
        dialog.add(recipientField);
        dialog.add(new JLabel("Account:"));
        dialog.add(accountComboBox);

        // Add a submit button
        JButton submitButton = new JButton("Create");
        submitButton.addActionListener(e -> handleCreateStandingOrder(accounts));
//
        dialog.add(submitButton);
        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Close the dialog
            }
        });
        dialog.add(goBackButton);
//
//        // Display the dialog
        dialog.setVisible(true);
//    }
    }

    private boolean validatePIN(String PIN){
        return PIN.equals(user.getPin());
    }

    private void handleCreateStandingOrder (ArrayList<Account> accounts) {
        Account selectedAccount = getSelectedAccount(accountComboBox, accounts);
        try {
            StandingOrder standingOrder = new StandingOrder(nameField.getText(), amountField.getText(), String.valueOf(selectedAccount.getAccountId()), recipientField.getText());
            int attempts = 3;
            while (attempts > 0) {
                String PIN = JOptionPane.showInputDialog(this, "Enter your PIN: (Attempts left: " + attempts + ")");
                if (PIN == null)
                    break;
                else if (!validatePIN(PIN)) {
                    attempts--;
                    JOptionPane.showMessageDialog(this, "Incorrect PIN!", "PIN error", JOptionPane.ERROR_MESSAGE);
                } else {
                    StandingOrder.registerStandingOrder(manager, standingOrder);
                    JOptionPane.showMessageDialog(this, "Created successfully!");
                    break;
                }
            }
            updateSOList();
        } catch (NumberFormatException | InvalidNameException | InvalidAmountException | InvalidAccountNumberException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    private Account getSelectedAccount (JComboBox < String > comboBox, ArrayList < Account > accounts){
        int selectedIndex = comboBox.getSelectedIndex();
        return selectedIndex >= 0 ? accounts.get(selectedIndex) : null;
    }

    private void updateSOList () throws SQLException {
        if (user != null) {
            listModel.clear();
            soMap.clear();
            ArrayList<StandingOrder> standingOrders = new ArrayList<>(manager.findUsersOrders(user.getId()));
            for (StandingOrder order : standingOrders) {
                listModel.addElement(order.getName());
                soMap.put(order.getName(), order);
            }
        }
    }
    private void displaySODetails (StandingOrder selectedSO){
        if (selectedSO != null) {
            nameLabel.setText("Name: " + selectedSO.getName());
            startDateLabel.setText("Date started: " + selectedSO.getDateStarted());
            senderIdLabel.setText("From which account: " + selectedSO.getSourceAccountId());
            recipientIdLabel.setText("To which account: " + selectedSO.getTargetAccountId());
            amountLabel.setText("How much do you want to send: " + selectedSO.getAmount().toString());
        }
    }

    public void setUser (User user){
        this.user = user;
        try {
            updateSOList();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "BD fault");
        }
    }
}
