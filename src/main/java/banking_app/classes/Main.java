package banking_app.classes;

import connections.ConnectionManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConnectionManager manager = new ConnectionManager();

        Menu menu = new Menu(manager);
        menu.mainMenu();
        
    }
}
