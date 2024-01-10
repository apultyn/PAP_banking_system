package banking_app.classes;

import connections.ConnectionManager;

import java.sql.SQLException;
import java.util.List;


public class TransferHistory {
    User user;
    long accountId;
    ConnectionManager manager;
    private final List<Transfer> transfers;

    public TransferHistory(User user, long accountId, ConnectionManager manager) throws SQLException {
        this.user = user;
        this.accountId = accountId;
        this.transfers = manager.findTransfersByAccount(accountId);
        this.manager = manager;
    }

    public void printTransferHistory() throws SQLException {
        String transferString;
        String sign;
        Account account = this.manager.findAccount(this.accountId);

        System.out.println("========= Historia transakcji ===========");
        System.out.println("Nazwa: " + account.getName());
        System.out.println("Numer rachunku: " + account.getAccountId());

        for(Transfer transfer: transfers) {
            if (transfer.getSourceId() == this.accountId) {
                sign = "-";
            } else {
                sign = "+";
            }
            System.out.println();

            String amount = String.format("%.2f", transfer.getAmount());
            transferString = "Tytuł: " + transfer.getTitle() + " Kwota: "+ sign + amount + "pln Data:" + transfer.getDate()
                    + "\nNadawca: "  + manager.findAccount(transfer.getSourceId()).getName() + " Numer: " + transfer.getSourceId()
                    + "\nOdbiorca: " + manager.findAccount(transfer.getTargetId()).getName() + " Numer: " + transfer.getTargetId();
            System.out.println(transferString);

        }

        System.out.println("=========================================");
    }
}
