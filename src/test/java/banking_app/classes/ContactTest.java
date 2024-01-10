package banking_app.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void getContactId() {
        Contact contact = new Contact(100, "Contact Name", 100000000023L);
        assert contact.getContactId() == 100;
    }

    @Test
    void getName() {
        Contact contact = new Contact(100, "Contact Name", 100000000023L);
        assert contact.getName().equals("Contact Name");
    }

    @Test
    void getAccountId() {
        Contact contact = new Contact(100, "Contact Name", 100000000023L);
        assert contact.getAccountId() == 100000000023L;
    }
}