package banking_app.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Contact {
    private final int contactId;
    private String name;
    private long accountId;

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

//    public static Contact createContact(ConnectionManager manager, String name, long accountId) throws SQLException {
//        manager.createContact(name, accountId);
//        return manager.findContact(email);
//    }

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
