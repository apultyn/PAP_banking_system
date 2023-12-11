package banking_app.classes;

import connections.ConnectionManager;

import java.sql.SQLException;
import java.util.List;


public class TransactionHistory {
    User user;
    long accountId;
    ConnectionManager manager;
    private  List<Transaction> transactions;

    public TransactionHistory(User user, long accountId, ConnectionManager manager) throws SQLException {
        this.user = user;
        this.accountId = accountId;
        this.transactions = manager.findTransactionsByAccount(accountId);
        this.manager = manager;
    }

    public void printTransactionHistory() throws SQLException {
        String transactionString;
        String sign;
        Account account = this.manager.findAccount(this.accountId);

        System.out.println("========= Historia transakcji ===========");
        System.out.println("Rachunek numer: " + account.getName());

        for(Transaction transaction: transactions) {
            if (transaction.getSourceId() == this.accountId) {
                sign = "-";
            } else {
                sign = "+";
            }
            System.out.println();

            String amount = String.format("%.2f", transaction.getAmount());
            transactionString = "Tytuł: " + transaction.getTitle() + " Kwota: "+ sign + amount + "zł Data:" + transaction.getDate()
                    + "\nNadawca: "  + manager.findAccount(transaction.getSourceId()).getName() + " Numer: " + transaction.getSourceId()
                    + "\nOdbiorca: " + manager.findAccount(transaction.getTargetId()).getName() + " Numer: " + transaction.getTargetId();
            System.out.println(transactionString);

        }

        System.out.println("=========================================");
    }
}
