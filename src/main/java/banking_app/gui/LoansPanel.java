package banking_app.gui;

import banking_app.classes.Account;
import banking_app.classes.Loan;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoansPanel extends JPanel {
    private User user;
    private final ConnectionManager manager;
    private final JPanel cardPanel;
    private final JPanel detailsPanel;
    private final JButton backButton;
    private final JButton registerLoan;
    private final JList<String> loansList;
    private final DefaultListModel<String> listModel;
    private final JTextArea loansDetails;
    private final JLabel idLabel;
    private final JLabel amountLabel;
    private final JLabel startDateLabel;
    private final JLabel rateLabel;
    private final JLabel endDateLabel;
    private final JLabel ownerAccLabel;
    private final JLabel fixedRateLabel;
    Map<String, Loan> loanMap = new HashMap<>();

    public LoansPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.manager = manager;
        this.cardPanel = cardPanel;
        this.setName(panelName);

        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        loansList = new JList<>(listModel);
        add(new JScrollPane(loansList), BorderLayout.WEST);

        loansDetails = new JTextArea(10, 30);
        loansDetails.setEditable(false);
        add(loansDetails, BorderLayout.CENTER);

        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        idLabel = new JLabel("Loan id:");
        amountLabel = new JLabel("How much do you want to send:");
        rateLabel = new JLabel("Rate of the loan: ");
        fixedRateLabel = new JLabel("Amount to pay each month: ");
        startDateLabel = new JLabel("Date started:");
        endDateLabel = new JLabel("From which account:");
        ownerAccLabel = new JLabel("To which account the loan is bound");

        detailsPanel.add(idLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(amountLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(fixedRateLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(startDateLabel);
        detailsPanel.add(new JLabel());
        detailsPanel.add(endDateLabel);
        detailsPanel.add(ownerAccLabel);

        add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Back");
        registerLoan = new JButton("Create New Loan");
        buttonPanel.add(backButton);
        buttonPanel.add(registerLoan);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e-> cardLayout.show(cardPanel, "User"));

        loansList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedName = loansList.getSelectedValue();
                Loan selectedLoan = loanMap.get(selectedName);
                displayASDetails(selectedLoan);
            }
        });

        registerLoan.addActionListener(e->{
            try {
                createNewStandingOrder();
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        try {
            updateLoanList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createNewStandingOrder() throws SQLException {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create Loan");
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(0, 2));

        ArrayList<Account> accounts = new ArrayList<>(manager.findUsersAccounts(user.getId()));

        JTextField amountField = new JTextField();
        JTextField rateField = new JTextField();
        JTextField endDateField = new JTextField();
        JComboBox<String> accountComboBox = new JComboBox<>();

        for (Account account : accounts) {
            accountComboBox.addItem(account.getName());
        }

        dialog.add(new JLabel("Amount:"));
        dialog.add(amountField);
        dialog.add(new JLabel("Rate:"));
        dialog.add(rateField);
        dialog.add(new JLabel("Ending date:"));
        dialog.add(endDateField);
        dialog.add(new JLabel("Choose the account: "));
        dialog.add(accountComboBox);

        JButton submitButton = new JButton("Create");
        submitButton.addActionListener(e -> {
            Account selectedAccount = getSelectedAccount(accountComboBox, accounts);
            try {
                String amount = amountField.getText();
                String rate = rateField.getText();
                String end = endDateField.getText();
                Loan.createLoan(manager, amount, rate, end , String.valueOf(selectedAccount.getAccountId()) , user);
                JOptionPane.showMessageDialog(dialog, "Loan created");
                dialog.dispose();
                updateLoanList();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Wrong input!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Wrong Date!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.add(submitButton);
        JButton goBackButton = new JButton("Back");
        goBackButton.addActionListener(e -> {
            dialog.dispose();
        });
        dialog.add(goBackButton);

        dialog.setLocationRelativeTo(SwingUtilities.findPanelByName(cardPanel, "Loans"));
        dialog.setVisible(true);
    }

    private Account getSelectedAccount(JComboBox<String> comboBox, ArrayList<Account> accounts) {
        int selectedIndex = comboBox.getSelectedIndex();
        return selectedIndex >= 0 ? accounts.get(selectedIndex) : null;
    }

    private void updateLoanList() throws SQLException {
        if (user != null) {
            listModel.clear();
            loanMap.clear();
            ArrayList<Loan> loans = new ArrayList<>(manager.findUsersLoans(user.getId()));
            for (Loan loan : loans) {
                String loanId = String.valueOf(loan.getLoanId());
                listModel.addElement(loanId);
                loanMap.put(loanId, loan);
            }
        }
    }

    private void displayASDetails(Loan selectedLoan) {
        if (selectedLoan != null) {
            idLabel.setText("Loan id: " + selectedLoan.getLoanId());
            amountLabel.setText("How much do you want to send: " + selectedLoan.getAmount());
            rateLabel.setText("Rate of the loan: " + selectedLoan.getRate());
            fixedRateLabel.setText("Amount to pay each month: " + selectedLoan.getFixed());
            startDateLabel.setText("Date started: " + selectedLoan.getStart());
            endDateLabel.setText("From which account: " + selectedLoan.getEnd());
            ownerAccLabel.setText("To which account the loan is bound: " + selectedLoan.getOwnerAccId());
        }
    }

    public void setUser (User user) {
        this.user = user;
        try {
            updateLoanList();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "BD fault", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
