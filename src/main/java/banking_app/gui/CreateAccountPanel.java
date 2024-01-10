package banking_app.gui;

import banking_app.classes.User;
import banking_exceptions.InvalidAmountException;
import banking_exceptions.InvalidNameException;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import static banking_app.gui.SwingUtilities.addLabelAndComponent;
import static banking_app.gui.SwingUtilities.resetComponents;

public class CreateAccountPanel extends JPanel {
    private User user;
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JLabel createAccountLabel;
    private JTextField accountNameField;
    private JTextField transferLimit;
    private JButton createAccountButton;
    private JButton backButton;
    public CreateAccountPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) throws SQLException {
        this.setName(panelName);
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        add(createAccountLabel = new JLabel("New account"), gbc);
        createAccountLabel.setFont(new Font(createAccountLabel.getFont().getFontName(), Font.BOLD, 24));
        gbc.gridwidth = 1;
        gbc.gridy++;
        addLabelAndComponent(this, "Account name:", accountNameField = new JTextField(20), gbc);
        addLabelAndComponent(this, "Transfer limit:", transferLimit = new JTextField(20), gbc);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        createAccountButton = new JButton("Create account");
        backButton = new JButton("Back");
        createAccountButton.addActionListener(e -> {
            handleCreateAccount();
        });
        backButton.addActionListener(e -> handleBackButton());
        add(createAccountButton, gbc);
        gbc.gridy++;
        add(backButton, gbc);
    }

    private void handleCreateAccount() {
        try {
            user.createAccount(manager, accountNameField.getText(), transferLimit.getText());
            JOptionPane.showMessageDialog(this, "Account created!");
            UserProfilePanel userProfilePanel = (UserProfilePanel) SwingUtilities.findPanelByName(cardPanel, "User");
            if (userProfilePanel != null) {
                userProfilePanel.setAccounts(manager.findUsersAccounts(user.getId()));
                cardLayout.show(cardPanel, "User");
                resetComponents(this);
            }
        } catch (InvalidNameException | InvalidAmountException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void handleBackButton () {
        resetComponents(this);
        cardLayout.show(cardPanel, "User");
    }
}
