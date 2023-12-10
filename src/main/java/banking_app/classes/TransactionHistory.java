package banking_app.classes;

import connections.ConnectionManager;

import java.sql.SQLException;
import java.util.List;


public class TransactionHistory {
    User user;
    private  List<Transaction> transactions;

    public TransactionHistory(User user, ConnectionManager manager) throws SQLException {
        this.user = user;
        this.transactions = manager.findTransactionsByUser(user.getId());
    }

    public void printTransactionHistory() {
        String transactionString;
        String sign;

        System.out.println("========= Historia transakcji ============");

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
