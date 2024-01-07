package banking_app.gui;

import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;

public class AutomaticSavingsGui extends JPanel {
    private User user;
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton viewSavingsButton;
    private JButton registerSavings;
    public AutomaticSavingsGui(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.setName(panelName);
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e-> cardLayout.show(cardPanel, "User"));
        add(backButton);

        add(new JLabel("Automatyczne oszczedzanie"));

        viewSavingsButton = new JButton("View automatic savings");
        viewSavingsButton.addActionListener(e->foo());
        add(viewSavingsButton);

        registerSavings = new JButton("Create new automatic savings");
        registerSavings.addActionListener(e->handleCreateSaving());
        add(registerSavings);

    }
    public void foo() {
        return;
    }

    public void handleCreateSaving(){
        CreateAutomaticSavingsPanel createAutomaticSavingsPanel = (CreateAutomaticSavingsPanel) SwingUtilities.findPanelByName(cardPanel, "CreateSaving");
        if (createAutomaticSavingsPanel != null) {
            createAutomaticSavingsPanel.setUser(user);
            cardLayout.show(cardPanel, "CreateSaving");
        }
    }

    public void setUser (User user) {
        this.user = user;

    }
}
