package banking_app.classes;

import connections.ConnectionManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConnectionManager manager = new ConnectionManager();
//        manager.createAccount("Konto główne", BigDecimal.valueOf(2000.23), 24);
        User user = manager.findUser("danielek@op.pl");
        user.createAccount(manager);
//        user.makeTransaction(manager);
        //manager.registerTransaction("Przecinek5", 8999009.25f, 1, 1000000000000003L, 1000000000000003L);
//        User.register(manager);
        //Menu menu = new Menu(manager);
        //menu.menu();
//        manager.createAccount("Konto główne", 200.0f, 25);
        //System.out.println();
//        manager.findUsersAccounts(21).forEach(a->
//                System.out.println(a.getAccountId())
//        );
//        User.makeTransaction(manager);
//        User user = User.login(manager);
//        System.out.println(user.getName());
//        String outcome = manager.example();
//        Transaction newTransaction  = new Transaction(3, 4, 32.00f, "Pierwszy");
//        Transaction.registerTransaction(manager, newTransaction);
    }
}
