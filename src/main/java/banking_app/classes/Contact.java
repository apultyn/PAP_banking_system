package banking_app.classes;

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
