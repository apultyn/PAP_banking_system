package banking_app.gui;

import banking_app.classes.AutomaticSaving;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class AutomaticSavingsGui extends JPanel {
    private User user;
    private ArrayList<AutomaticSaving> savings;

    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton viewSavingsButton;
    private JButton registerSavings;
    private JList<String> asList;
    private JLabel nameLabel, startDateLabel, senderIdLabel, recieverIdLabel, amountLabel;
    public AutomaticSavingsGui(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.setName(panelName);

        setLayout(new BorderLayout());

        nameLabel = new JLabel("Name:");
        startDateLabel = new JLabel("Date started:");
        senderIdLabel = new JLabel("From which account:");
        recieverIdLabel = new JLabel("To which account:");
        amountLabel = new JLabel("How much:");


        JButton backButton = new JButton("cofnij");
        backButton.addActionListener(e-> cardLayout.show(cardPanel, "User"));

        add(new JLabel("Automaatic savings panel"));

        registerSavings = new JButton("Create new automatic savings");
        registerSavings.addActionListener(e->handleCreateSaving());

        add(new JLabel("Your automatic savings: "));

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

    public void handleCreateSaving(){
        CreateAutomaticSavingsPanel createAutomaticSavingsPanel = (CreateAutomaticSavingsPanel) SwingUtilities.findPanelByName(cardPanel, "CreateSaving");
        if (createAutomaticSavingsPanel != null) {
            createAutomaticSavingsPanel.setUser(user);
            cardLayout.show(cardPanel, "CreateSaving");
        }
    }

    public void setUser (User user) {
        this.user = user;

        try {
            savings = new ArrayList<>(manager.findUsersSavings(user.getId()));
        } catch (SQLException a) {
            //JOptionPane.showMessageDialog(this, "Blad bazy");
            savings = new ArrayList<>();
        }
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (AutomaticSaving as : savings) {
            listModel.addElement(as.getName());
        }

        asList = new JList<>(listModel);
        asList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        asList.addListSelectionListener(e -> displaySelectedSaving());

        add(new JScrollPane(asList), BorderLayout.WEST);
    }

    public void displaySelectedSaving() {
        int selectedIndex = asList.getSelectedIndex();
        if (selectedIndex >= 0) {
            AutomaticSaving saving = savings.get(selectedIndex);
            nameLabel.setText("Name: " + saving.getName());
            startDateLabel.setText("Date started: " + saving.getDateStarted());
            senderIdLabel.setText("From which account: " + saving.getSourceAccountId());
            recieverIdLabel.setText("To which account: " + saving.getTargetAccountId());
            amountLabel.setText("How much: " + saving.getAmount());
        }

    }
}
