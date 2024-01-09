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

public class DepositPanel extends JPanel {
    private JList<String> depositList;
    private JLabel nameLabel;
    private JLabel amountLabel;
    private JLabel rateLabel;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel ownerAccIdLabel;
    private JButton backButton;
    private JButton createNewButton;
    private ArrayList<Deposit> deposits;
    private User user;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private ConnectionManager manager;


    public DepositPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) throws SQLException {
        this.setName(panelName);
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.manager = manager;

        setLayout(new BorderLayout());
        deposits = new ArrayList<>(manager.findUsersDeposits(user.getId()));
        for (Deposit deposit : deposits) {
            System.out.println(deposit);
        }

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Deposit deposit : deposits) {
            listModel.addElement(deposit.getName());
        }
        depositList = new JList<>(listModel);
        depositList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        depositList.addListSelectionListener(e -> displaySelectedDeposit());

        // Create labels for deposit information
        nameLabel = new JLabel("Name:");
        amountLabel = new JLabel("Amount:");
        rateLabel = new JLabel("Rate:");
        startDateLabel = new JLabel("Start Date:");
        endDateLabel = new JLabel("End Date:");
        ownerAccIdLabel = new JLabel("Owner Account Number:");

        // Create buttons
        backButton = new JButton("Go Back");
        backButton.addActionListener(e -> goBack());
        createNewButton = new JButton("Create New Deposit");
        createNewButton.addActionListener(e -> {
            try {
                createNewDeposit(manager, user, listModel);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Create a panel for deposit information
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(6, 2));
        infoPanel.add(nameLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(amountLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(rateLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(startDateLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(endDateLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(ownerAccIdLabel);
        infoPanel.add(new JLabel());

        // Add components to the main panel
        add(new JScrollPane(depositList), BorderLayout.WEST);
        add(infoPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(createNewButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void displaySelectedDeposit() {
        int selectedIndex = depositList.getSelectedIndex();
        if (selectedIndex >= 0) {
            Deposit deposit = deposits.get(selectedIndex);
            nameLabel.setText("Name: " + deposit.getName());
            amountLabel.setText("Amount: " + deposit.getAmount());
            rateLabel.setText("Rate: " + deposit.getRate());
            startDateLabel.setText("Start Date: " + deposit.getStart());
            endDateLabel.setText("End Date: " + deposit.getEnd());
            ownerAccIdLabel.setText("Owner Account Number: " + deposit.getOwnerAccId());
        }
    }

    private void goBack() {
        // Implement go back functionality here
    }

    private void updateDepositList(ConnectionManager manager, DefaultListModel<String> listModel, User user) {
        listModel.clear();
        try {
            deposits = new ArrayList<>(manager.findUsersDeposits(user.getId()));
            for (Deposit deposit : deposits) {
                listModel.addElement(deposit.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    private void createNewDeposit(ConnectionManager manager, User user, DefaultListModel<String> listModel) throws SQLException {
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
                    updateDepositList(manager, listModel, user ); // Update deposit list in main panel

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
    // New method to safely get the selected Account object
    private Account getSelectedAccount(JComboBox<String> comboBox, ArrayList<Account> accounts) {
        int selectedIndex = comboBox.getSelectedIndex();
        return selectedIndex >= 0 ? accounts.get(selectedIndex) : null;
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
//    public static void main(String[] args) throws SQLException, NotEnoughFundsException, DepositNameExistingException, AccountNotFoundException {
//        ConnectionManager manager = new ConnectionManager();
//        User user = manager.findUser("przykladowy.mail@pw.edu.pl");
////        Deposit deposit = new Deposit(0, "czwarte", new BigDecimal(19.94), new BigDecimal(2),
////                1000000000000047L, new Date(2024, 1, 10), new Date(2024, 10, 11));
////        deposit.createDeposit(manager);
//        DepositPanel depositPanel = new DepositPanel(manager, null, null, "Deposit");
//
//        JFrame frame = new JFrame("Bank Application");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 600);
//        frame.add(depositPanel);
//        frame.setVisible(true);
//    }
}


