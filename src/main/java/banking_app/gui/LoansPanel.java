package banking_app.gui;

import banking_app.classes.Loan;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoansPanel extends JPanel {
    private User user;
    private ArrayList<Loan> loans;

    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton viewSavingsButton;
    private JButton registerLoans;
    private JList<String> loansList;
    private JLabel amountLabel, startDateLabel, rateLabel, endDateLabel, ownerLabel;
    public LoansPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.setName(panelName);

        setLayout(new BorderLayout());

        add( new JLabel("Loans panel"));

        amountLabel = new JLabel("Amount:");
        startDateLabel = new JLabel("Date started:");
        rateLabel = new JLabel("Rate:");
        endDateLabel = new JLabel("Closing date:");
        ownerLabel = new JLabel("Which account:");


        JButton backButton = new JButton("Back");
        backButton.addActionListener(e-> cardLayout.show(cardPanel, "User"));

        add(new JLabel("Loans"));

        registerLoans = new JButton("Create new Loan");
        registerLoans.addActionListener(e->handleCreateLoan());

        add(new JLabel("Your loans: "));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 2));
        infoPanel.add(amountLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(startDateLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(rateLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(endDateLabel);
        infoPanel.add(new JLabel());
        infoPanel.add(ownerLabel);
        infoPanel.add(new JLabel());

        add(infoPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(registerLoans);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    public void handleCreateLoan(){
        CreateLoansPanel createLoansPanel = (CreateLoansPanel) SwingUtilities.findPanelByName(cardPanel, "CreateLoans");
        if (createLoansPanel != null) {
            createLoansPanel.setUser(user);
            cardLayout.show(cardPanel, "CreateLoans");
        }
    }

    public void setUser (User user) {
        this.user = user;

        try {
            loans = new ArrayList<>(manager.findUsersLoans(user.getId()));
        } catch (SQLException a) {
            //JOptionPane.showMessageDialog(this, "Blad bazy");
            loans = new ArrayList<>();
        }
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Loan l : loans) {
            listModel.addElement(String.valueOf(l.getLoanId()));
        }

        loansList = new JList<>(listModel);
        loansList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loansList.addListSelectionListener(e -> displaySelectedSaving());

        add(new JScrollPane(loansList), BorderLayout.WEST);
    }

    public void displaySelectedSaving() {
        int selectedIndex = loansList.getSelectedIndex();
        if (selectedIndex >= 0) {
            Loan loan = loans.get(selectedIndex);
            amountLabel.setText("Amount: " + loan.getAmount());
            startDateLabel.setText("Date started: " + loan.getStart());
            rateLabel.setText("Rate: " + loan.getRate());
            endDateLabel.setText("Closing date: " + loan.getEnd());
            ownerLabel.setText("Which account: " + loan.getLoanId());
        }

    }
}

