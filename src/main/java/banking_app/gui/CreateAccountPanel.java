package banking_app.gui;

import banking_app.classes.Account;
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
    private final ConnectionManager manager;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JLabel createAccountLabel;
    private final JTextField accountNameField;
    private final JTextField transferLimit;
    private final JButton createAccountButton;
    private final JButton backButton;

    public CreateAccountPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
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
            Account account = User.createAccount(manager, this.user, accountNameField.getText(), transferLimit.getText());
            manager.createAccount(account);

            JOptionPane.showMessageDialog(this, "Account created!");
            UserProfilePanel userProfilePanel = (UserProfilePanel) SwingUtilities.findPanelByName(cardPanel, "User");
            if (userProfilePanel != null) {
                userProfilePanel.setAccounts(manager.findUsersAccounts(user.getId()));
                if (user != null){
                    userProfilePanel.setUser(user);
                }
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
        UserProfilePanel userPanel = (UserProfilePanel) SwingUtilities.findPanelByName(cardPanel,"User");
        if (userPanel != null && user != null){
            userPanel.setUser(user);
        }
        cardLayout.show(cardPanel, "User");
    }
}
