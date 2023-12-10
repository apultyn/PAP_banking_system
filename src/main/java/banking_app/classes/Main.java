package banking_app.classes;

import connections.ConnectionManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConnectionManager manager = new ConnectionManager();
        String outcome = manager.example();
        Transaction newTransaction  = new Transaction(1, 2, 32.00f, "Pierwszy");
        Transaction.registerTransaction(manager, newTransaction);
    }
}
