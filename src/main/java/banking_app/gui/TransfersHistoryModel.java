package banking_app.gui;

import banking_app.classes.Transfer;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TransfersHistoryModel extends AbstractTableModel {
    private final List<Transfer> transfers;
    private final long currentAccountId;
    private final String[] columnNames = {"Date", "Title", "Sender", "Receiver", "Amount"};

    public TransfersHistoryModel(List<Transfer> transfers, long currentAccountId) {
        this.transfers = transfers;
        this.currentAccountId = currentAccountId;
    }
    @Override
    public int getRowCount() {
        return transfers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transfer transfer = transfers.get(rowIndex);
        boolean isIncoming = transfer.getTargetId() == currentAccountId;

        switch (columnIndex) {
            case 0: return transfer.getDate();
            case 1: return transfer.getTitle();
            case 2: return transfer.getSourceId();
            case 3: return transfer.getTargetId();
            case 4: return (isIncoming ? "+" : "-") + String.format("%.2f pln", transfer.getAmount());
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
