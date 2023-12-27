package banking_app.classes;

import connections.ConnectionManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Main run!");
        ConnectionManager manager = new ConnectionManager();
        System.out.println("Connection established!");
        /*manager.setTransactionLimit(1000000000000043L, 503.02f);
        Date end = new Date(Date.valueOf("2023-12-29").getTime());
        manager.createDeposit("drugi", new BigDecimal("10"), new BigDecimal("7.3"), 87, end);*/

        Menu menu = new Menu(manager);
        menu.menu();
    }
}
