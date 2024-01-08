package banking_app.gui;

import banking_app.classes.StandingOrder;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class StandingOrdersPanel extends JPanel {
    private User user;
    private ArrayList<StandingOrder> orders;

    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton viewSavingsButton;
    private JButton registerSavings;
    private JList<String> asList;
    private JLabel nameLabel, startDateLabel, senderIdLabel, recieverIdLabel, amountLabel;
    public StandingOrdersPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel) {
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());

        nameLabel = new JLabel("Name:");
        startDateLabel = new JLabel("Date started:");
        senderIdLabel = new JLabel("From which account:");
        recieverIdLabel = new JLabel("To which account:");
        amountLabel = new JLabel("How much:");


        JButton backButton = new JButton("Back");
        backButton.addActionListener(e-> cardLayout.show(cardPanel, "User"));

        registerSavings = new JButton("Create new standing order");
        registerSavings.addActionListener(e->handleCreateOrder());

        add(new JLabel("Your standing orders: "));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 2));
        infoPanel.add(nameLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(startDateLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(senderIdLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(recieverIdLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(amountLabel);
        infoPanel.add(new JLabel());

        add(infoPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(registerSavings);
        add(buttonPanel, BorderLayout.SOUTH);

    }
    public void foo() {
        return;
    }

    public void handleCreateOrder(){
        CreateStandingOrdersPanel createStandingOrdersPanel = (CreateStandingOrdersPanel) cardPanel.getComponent(8);
        createStandingOrdersPanel.setUser(user);
        cardLayout.show(cardPanel, "CreateOrder");
    }

    public void setUser (User user) {
        this.user = user;

        try {
            orders = new ArrayList<>(manager.findUsersOrders(user.getId()));
        } catch (SQLException a) {
            //JOptionPane.showMessageDialog(this, "Blad bazy");
            orders = new ArrayList<>();
        }
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (StandingOrder o : orders) {
            listModel.addElement(o.getName());
        }

        asList = new JList<>(listModel);
        asList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        asList.addListSelectionListener(e -> displaySelectedOrder());

        add(new JScrollPane(asList), BorderLayout.WEST);
    }
    public void displaySelectedOrder() {
        int selectedIndex = asList.getSelectedIndex();
        if (selectedIndex >= 0) {
            StandingOrder order = orders.get(selectedIndex);
            nameLabel.setText("Name: " + order.getName());
            startDateLabel.setText("Date started: " + order.getDateStarted());
            senderIdLabel.setText("From which account: " + order.getSourceAccountId());
            recieverIdLabel.setText("To which account: " + order.getTargetAccountId());
            amountLabel.setText("How much: " + order.getAmount());
        }

    }
}
