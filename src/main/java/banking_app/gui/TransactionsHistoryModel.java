package banking_app.gui;

import banking_app.classes.Transaction;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TransactionsHistoryModel extends AbstractTableModel {
    private List<Transaction> transactions;
    private final String[] columnNames = {"Title", "Amount"};

    public TransactionsHistoryModel(List<Transaction> transactions) {
        this.transactions = transactions;
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
        switch (columnIndex) {
            case 0:
                return transaction.getTitle();
            case 1:
                return String.format("%.2f zł", transaction.getAmount());
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
