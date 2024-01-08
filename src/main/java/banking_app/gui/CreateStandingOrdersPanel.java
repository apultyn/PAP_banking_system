package banking_app.gui;

import banking_app.classes.StandingOrder;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CreateStandingOrdersPanel extends JPanel{
    private User user;
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField nameField;
    private JTextField recieverField;
    private JTextField senderField;
    private JTextField amountField;
    private JButton backButton, confirmButton;
    public CreateStandingOrdersPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.setName(panelName);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Create new standing order"));

        backButton = new JButton("Back");
        backButton.addActionListener(e-> cardLayout.show(cardPanel, "StandingOrders"));
        add(backButton);

        add(new JLabel("Choose name"));

        nameField = new JTextField(10);
        add(nameField);

        add(new JLabel("Write account number from which to send"));

        senderField = new JTextField(20);
        add(senderField);

        add(new JLabel("Write account number to which you want to send send"));

        recieverField = new JTextField(20);
        add(recieverField);

        add(new JLabel("Write amount that you wanto to send"));

        amountField = new JTextField(20);
        add(amountField);

        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e-> registerStanding());
        add(confirmButton);

    }
    public void registerStanding() {
        String name = nameField.getText();
        String senderId = senderField.getText();
        String recieverId = recieverField.getText();
        String amount = amountField.getText();
        try {
            StandingOrder.registerStandingOrder(manager, user, name, senderId, recieverId, amount);
            JOptionPane.showMessageDialog(this, "Created");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong input");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database fault");
        }
    }
    public void setUser(User u) {
        user = u;
    }
}
