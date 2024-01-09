package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.Deposit;
import banking_app.classes.User;
import banking_exceptions.AccountNotFoundException;
import banking_exceptions.DepositNameExistingException;
import banking_exceptions.MissingInformationException;
import banking_exceptions.NotEnoughFundsException;
import com.toedter.calendar.JDateChooser;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepositPanel extends JPanel {
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel, detailsPanel;
    private JList<String> depositList;
    private DefaultListModel<String> listModel;
    private JTextArea depositDetails;
    private JButton goBackButton, createNewDepositButton;
    private User user;
    private JLabel nameLabel, amountLabel, rateLabel, startDateLabel, endDateLabel, ownerAccNumLabel;
    private Map<String, Deposit> depositMap = new HashMap<>();

    public DepositPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) throws SQLException {
        this.manager = manager;
        this.setName(panelName);
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        setLayout(new BorderLayout());

        // Deposit list
        listModel = new DefaultListModel<>();
        depositList = new JList<>(listModel);
        add(new JScrollPane(depositList), BorderLayout.WEST);

        // Deposit details
        depositDetails = new JTextArea(10, 30);
        depositDetails.setEditable(false);
        add(depositDetails, BorderLayout.CENTER);

        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        nameLabel = new JLabel("Name: ");
        amountLabel = new JLabel("Amount: ");
        rateLabel = new JLabel("Rate: ");
        startDateLabel = new JLabel("Start Date: ");
        endDateLabel = new JLabel("End Date: ");
        ownerAccNumLabel = new JLabel("Owner Account Number: ");

        detailsPanel.add(nameLabel);
        detailsPanel.add(amountLabel);
        detailsPanel.add(rateLabel);
        detailsPanel.add(startDateLabel);
        detailsPanel.add(endDateLabel);
        detailsPanel.add(ownerAccNumLabel);

        add(detailsPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        goBackButton = new JButton("Go Back");
        createNewDepositButton = new JButton("Create New Deposit");
        buttonPanel.add(goBackButton);
        buttonPanel.add(createNewDepositButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        depositList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedName = depositList.getSelectedValue();
                Deposit selectedDeposit = depositMap.get(selectedName);
                displayDepositDetails(selectedDeposit);
            }
        });

        goBackButton.addActionListener(e -> cardLayout.show(cardPanel, "User"));
        createNewDepositButton.addActionListener(e -> {
            try {
                createNewDeposit(listModel);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        try {
            updateDepositList();  // Refresh the deposit list every time the panel is shown
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the SQLException appropriately
        }
    }

    private void createNewDeposit(DefaultListModel<String> listModel) throws SQLException {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create New Deposit");
        dialog.setSize(400, 300); // Set the size of the dialog
        dialog.setLayout(new GridLayout(0, 2)); // Using GridLayout for simplicity

        ArrayList<Account> accounts = new ArrayList<>(manager.findUsersAccounts(user.getId()));

        JTextField nameField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField rateField = new JTextField();
        JDateChooser endDateChooser = new JDateChooser(); // Date chooser for end date
        JComboBox<String> accountComboBox = new JComboBox<>();

        // Populate accountComboBox with account names
        for (Account account : accounts) {
            accountComboBox.addItem(account.getName());
        }

        // Adding form fields to the dialog
        dialog.add(new JLabel("Deposit Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Amount:"));
        dialog.add(amountField);
        dialog.add(new JLabel("Rate:"));
        dialog.add(rateField);
        dialog.add(new JLabel("End Date:"));
        dialog.add(endDateChooser);
        dialog.add(new JLabel("Account:"));
        dialog.add(accountComboBox);

        // Add a submit button
        JButton submitButton = new JButton("Create");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account selectedAccount = getSelectedAccount(accountComboBox, accounts);
                long accountId = selectedAccount.getAccountId();
                try {
                    Deposit deposit = getDeposit(selectedAccount, nameField, amountField, rateField, endDateChooser, accountComboBox);
                    deposit.createDeposit(manager);
                    JOptionPane.showMessageDialog(dialog, "Deposit created");
                    dialog.dispose(); // Close the creating deposit window
                    updateDepositList(); // Update deposit list in main panel

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (NotEnoughFundsException ex) {
                    JOptionPane.showMessageDialog(dialog, "Not enough funds on account!");
                } catch (DepositNameExistingException ex) {
                    JOptionPane.showMessageDialog(dialog, "You already have deposit with this name on this account!");
                } catch (AccountNotFoundException ex) {
                    JOptionPane.showMessageDialog(dialog, "No such account in your user!");
                } catch (MissingInformationException ex) {
                    JOptionPane.showMessageDialog(dialog, "Missing information!");
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

    private static Deposit getDeposit(Account selectedAccount,
                                      JTextField nameField,
                                      JTextField amountField,
                                      JTextField rateField,
                                      JDateChooser endDateChooser,
                                      JComboBox<String> accountComboBox) throws MissingInformationException {
        if (nameField.getText().trim().isEmpty() ||
                amountField.getText().trim().isEmpty() ||
                rateField.getText().trim().isEmpty() ||
                endDateChooser.getDate() == null ||
                accountComboBox.getSelectedItem() == null) {
            throw new MissingInformationException("All fields are required.");
        }
        String name = nameField.getText();
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(amountField.getText()));
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(rateField.getText()));
        long ownerAccountId = selectedAccount.getAccountId();
        java.sql.Date endDate = new java.sql.Date(endDateChooser.getDate().getTime());
        Deposit deposit = new Deposit(
                0,
                name,
                amount,
                rate,
                ownerAccountId,
                null,
                endDate
        );
        return deposit;
    }


    private Account getSelectedAccount(JComboBox<String> comboBox, ArrayList<Account> accounts) {
        int selectedIndex = comboBox.getSelectedIndex();
        return selectedIndex >= 0 ? accounts.get(selectedIndex) : null;
    }


    private void updateDepositList() throws SQLException {
        if (user != null) {
            listModel.clear();
            depositMap.clear();
            ArrayList<Deposit> deposits = new ArrayList<>(manager.findUsersDeposits(user.getId()));
            for (Deposit deposit : deposits) {
                listModel.addElement(deposit.getName());
                depositMap.put(deposit.getName(), deposit);
            }
        }
    }

    private void displayDepositDetails(Deposit deposit) {
        if (deposit != null) {
            nameLabel.setText("Name: " + deposit.getName());
            amountLabel.setText("Amount: " + deposit.getAmount().toString());
            rateLabel.setText("Rate: " + deposit.getRate().toString());
            startDateLabel.setText("Start Date: " + deposit.getStart().toString());
            endDateLabel.setText("End Date: " + deposit.getEnd().toString());
            ownerAccNumLabel.setText("Owner Account Number: " + deposit.getOwnerAccId());
        }
    }

    public void setUser(User user) throws SQLException {
        this.user = user;
        updateDepositList();
    }

//    public static void main(String[] args) throws SQLException {
//        ConnectionManager manager = new ConnectionManager();
//        User user = manager.findUser("przykladowy.mail@pw.edu.pl");
//        System.out.println(user);
//        JFrame frame = new JFrame("Deposit Panel");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        DepositPanel depositPanel = new DepositPanel(manager, null, null, "Deposits");
//        depositPanel.setUser(user);
//        frame.add(depositPanel);
//        frame.pack();
//        frame.setVisible(true);
//    }
}
