package banking_app.gui;

import banking_app.classes.Transaction;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TransactionsHistoryModel extends AbstractTableModel {
    private final List<Transaction> transactions;
    private final long currentAccountId;
    private final String[] columnNames = {"Date", "Title", "Sender", "Receiver", "Amount"};

    public TransactionsHistoryModel(List<Transaction> transactions, long currentAccountId) {
        this.transactions = transactions;
        this.currentAccountId = currentAccountId;
    }
    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaction transaction = transactions.get(rowIndex);
        boolean isIncoming = transaction.getTargetId() == currentAccountId;

        switch (columnIndex) {
            case 0: return transaction.getDate(); // Format daty według potrzeb
            case 1: return transaction.getTitle();
            case 2: return transaction.getSourceId();
            case 3: return transaction.getTargetId();
            case 4: return (isIncoming ? "+" : "-") + String.format("%.2f zł", transaction.getAmount());
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
