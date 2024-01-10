package banking_app.classes;

import banking_exceptions.InvalidAccountNumberException;
import banking_exceptions.InvalidNameException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Contact {
    private final Integer contactId;
    private final String name;
    private final long accountId;

    public Contact(int contactId, String name, long accountId) {
        this.contactId = contactId;
        this.name = name;
        this.accountId = accountId;
    }

    public Contact(ResultSet resultSet) throws SQLException {
        this(resultSet.getInt("contact_id"),
                resultSet.getString("name"),
                resultSet.getLong("accounts_account_id"));
    }

    public Contact(String name, String recipientAccountNumber) throws InvalidNameException, InvalidAccountNumberException {
        if (name.isEmpty())
            throw new InvalidNameException("Name cannot be empty!");
        if (!recipientAccountNumber.matches("\\d{16}"))
            throw new InvalidAccountNumberException("Number must be 16 digits long!");

        this.contactId = null;
        this.name = name;
        this.accountId = Long.parseLong(recipientAccountNumber);
    }

    public int getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public long getAccountId() {
        return accountId;
    }
}
