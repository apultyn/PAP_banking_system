package banking_app.classes;

import connections.ConnectionManager;

import java.sql.SQLException;
import java.util.List;


public class TransactionHistory {
    User user;
    int accountId;
    ConnectionManager manager;
    private  List<Transaction> transactions;

    public TransactionHistory(User user, int accountId, ConnectionManager manager) throws SQLException {
        this.user = user;
        this.accountId = accountId;
        this.transactions = manager.findTransactionsByAccount(accountId);
        this.manager = manager;
    }

    public void printTransactionHistory() throws SQLException {
        String transactionString;
        String sign;
        Account account = this.manager.findAccount(this.accountId);

        System.out.println("========= Historia transakcji ============");
        System.out.println("Rachunek numer: " + account.getName());

        for(Transaction transaction: transactions) {
            if (transaction.getSourceId() == user.getId()) {
                sign = "-";
            } else {
                sign = "+";
            }

            transactionString = transaction.getTitle() + " Kwota: "+ sign + transaction.getAmount() + "zł Data:" + transaction.getDate()
                    + "\n Nadawca: " + transaction.getSourceId() + " Odbiorca: " + transaction.getTargetId();
            System.out.println(transactionString);
        }

        System.out.println("=========================================");
    }
}
