package banking_app.gui;

import banking_app.classes.Deposit;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class DepositPanel extends JPanel {
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JList<String> depositList;
    private DefaultListModel<String> listModel;
    private JTextArea depositDetails;
    private JButton goBackButton, createNewDepositButton;
    private User user;

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
                displayDepositDetails(selectedName);
            }
        });

        // Implement button actions here...
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
        listModel.clear();
        ArrayList<Deposit> deposits = new ArrayList<>(manager.findUsersDeposits(user.getId()));
        for (Deposit deposit : deposits) {
            listModel.addElement(deposit.getName());
        }
    }

    private void displayDepositDetails(String depositName) {
        // Fetch deposit details based on the depositName and update depositDetails JTextArea
        // Example: depositDetails.setText("Name: " + deposit.getName() + "\nAmount: " + deposit.getAmount() + ...);
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Additional methods like refreshPanel, button actions, etc.
    public static void main(String[] args) throws SQLException {
        ConnectionManager manager = new ConnectionManager();
        User user = manager.findUser("przykladowy.mail@pw.edu.pl");
        System.out.println(user);
        JFrame frame = new JFrame("Deposit Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DepositPanel depositPanel = new DepositPanel(manager, null, null, "Deposits");
        depositPanel.setUser(user);
        frame.add(depositPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
