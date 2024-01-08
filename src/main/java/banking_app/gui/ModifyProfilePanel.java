package banking_app.gui;

import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;

public class ModifyProfilePanel extends JPanel {
    private User user;

    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public ModifyProfilePanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setName(panelName);
        this.manager = manager;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        JButton returnButton = new JButton("Back");

        add(returnButton);

        returnButton.addActionListener(e -> cardLayout.show(cardPanel, "User"));
    }
    public void setUser(User setted_user) {
        user = setted_user;
    }
}
