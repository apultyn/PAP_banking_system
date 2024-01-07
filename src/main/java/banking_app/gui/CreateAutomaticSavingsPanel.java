package banking_app.gui;

import banking_app.classes.AutomaticSaving;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CreateAutomaticSavingsPanel extends JPanel {
    private User user;
    private ConnectionManager manager;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField nameField;
    private JTextField recieverField;
    private JTextField senderField;
    private JTextField amountField;
    private JButton backButton, confirmButton;
    public CreateAutomaticSavingsPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel) {
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        add(new JLabel("Create new automatic saving"));

        backButton = new JButton("cofnij");
        backButton.addActionListener(e-> cardLayout.show(cardPanel, "SavingsPanel"));
        add(backButton);

        add(new JLabel("Podaj nazwe"));

        nameField = new JTextField(10);
        add(nameField);

        add(new JLabel("Podaj konto z ktorego wysylasz"));

        senderField = new JTextField(20);
        add(senderField);

        add(new JLabel("Podaj konto na ktore wysylasz"));

        recieverField = new JTextField(20);
        add(recieverField);

        add(new JLabel("Podaj ile chcesz wysylac"));

        amountField = new JTextField(20);
        add(amountField);

        confirmButton = new JButton("Potwierdz");
        confirmButton.addActionListener(e-> registerAutomatic());
        add(confirmButton);

    }
    public void registerAutomatic() {
        String name = nameField.getText();
        String senderId = senderField.getText();
        String recieverId = recieverField.getText();
        String amount = amountField.getText();
        try {
            AutomaticSaving.registerAutomaticSaving(manager, user, name, senderId, recieverId, amount);
            JOptionPane.showMessageDialog(this, "Utworzono");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Zle dane");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Blad bazy");
        }
    }
    public void setUser(User u) {
        user = u;
    }
}
