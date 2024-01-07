package banking_app.gui;

import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import java.awt.*;

public class UserProfilePanel extends JPanel {
    private String name;
    private String surname;

    private JLabel nameLabel;
    private JLabel surnameLabel;

    private ConnectionManager manager;

    public UserProfilePanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.manager = manager;

        nameLabel = new JLabel("Name:");
        surnameLabel = new JLabel("Surname:");

        add(nameLabel);
        add(surnameLabel);
    }

    public void setUser(User user) {
        name = user.getName();
        surname = user.getSurname();
        nameLabel.setText("Name: " + name);
        surnameLabel.setText("Surname: " + surname);
    }
}
