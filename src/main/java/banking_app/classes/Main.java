package banking_app.classes;

import connections.ConnectionManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConnectionManager manager = new ConnectionManager();
        User user = User.login(manager);
        System.out.println(user.getName());
        String outcome = manager.example();
        Transaction newTransaction  = new Transaction(3, 4, 32.00f, "Pierwszy");
        Transaction.registerTransaction(manager, newTransaction);
    }
}
