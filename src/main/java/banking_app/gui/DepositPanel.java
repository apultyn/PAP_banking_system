package banking_app.gui;

import banking_app.classes.Deposit;
import banking_app.classes.User;
import banking_exceptions.AccountNotFoundException;
import banking_exceptions.DepositNameExistingException;
import banking_exceptions.NotEnoughFundsException;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
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

    public DepositPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, User user) throws SQLException {
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
        createNewButton.addActionListener(e -> createNewDeposit());

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

    private void createNewDeposit() {
        // Implement create new deposit functionality here
    }

    public static void main(String[] args) throws SQLException, NotEnoughFundsException, DepositNameExistingException, AccountNotFoundException {
        ConnectionManager manager = new ConnectionManager();
        User user = manager.findUser("przyklad@pw.edu.pl");
//        Deposit deposit = new Deposit(0, "czwarte", new BigDecimal(19.94), new BigDecimal(2),
//                1000000000000047L, new Date(2024, 1, 10), new Date(2024, 10, 11));
//        deposit.createDeposit(manager);
        DepositPanel depositPanel = new DepositPanel(manager, null, null, user);

        JFrame frame = new JFrame("Bank Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(depositPanel);
        frame.setVisible(true);
    }
}
