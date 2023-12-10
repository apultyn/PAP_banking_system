package banking_app.classes;

public class Contact {
    private final int contactId;
    private String name;
    private int userId;

    public Contact(int contactId, String name, int userId) {
        this.contactId = contactId;
        this.name = name;
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }
}
