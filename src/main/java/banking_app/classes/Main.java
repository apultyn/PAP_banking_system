package banking_app.classes;

import connections.ConnectionManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Main run!");
        ConnectionManager manager = new ConnectionManager();
        System.out.println("Connection established!");
        Menu menu = new Menu(manager);
        menu.mainMenu();
    }
}
