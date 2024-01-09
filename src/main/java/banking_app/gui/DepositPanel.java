package banking_app.gui;

import banking_app.classes.Deposit;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
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


    private void updateDepositList() throws SQLException {
        if (user != null) {
            listModel.clear();
            depositMap.clear();
            ArrayList<Deposit> deposits = new ArrayList<>(manager.findUsersDeposits(user.getId()));
            for (Deposit deposit : deposits) {
                listModel.addElement(deposit.getName());
                depositMap.put(deposit.getName(), deposit);
            }
        } else System.out.println("No user!");
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
