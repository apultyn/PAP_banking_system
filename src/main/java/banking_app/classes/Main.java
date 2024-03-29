package banking_app.classes;

import banking_app.gui.MainMenu;
import connections.ConnectionManager;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Main run!");
        ConnectionManager manager = new ConnectionManager();
        System.out.println("Connected");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainMenu a = new MainMenu();
            }
        });
    }
}
