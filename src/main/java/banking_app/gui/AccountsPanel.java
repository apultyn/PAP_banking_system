package banking_app.gui;

import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;

public class AccountsPanel extends JPanel {
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private User user;
    public AccountsPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName){
        this.setName(panelName);
        this.manager = manager;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;


    }

    public void setUser(User user){
        this.user = user;
    }
}
