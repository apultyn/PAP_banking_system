package banking_app.classes;

class UserTest {

    @org.junit.jupiter.api.Test
    void getId() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getId() == 2;
    }

    @org.junit.jupiter.api.Test
    void getName() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getName().equals("Jan");
    }

    @org.junit.jupiter.api.Test
    void getSurname() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getSurname().equals("Kowalski");
    }

    @org.junit.jupiter.api.Test
    void getEmail() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getEmail().equals("example@gm.pl");
    }

    @org.junit.jupiter.api.Test
    void getPassword() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getPassword().equals("Password123!");
    }

    @org.junit.jupiter.api.Test
    void getReset_code() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getReset_code().equals("1234");
    }

    @org.junit.jupiter.api.Test
    void getPin() {
        User user = new User(2, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
        assert user.getPin().equals("9999");
    }

    @org.junit.jupiter.api.Test
    void getAccounts() {
//        User user = new User(83, "Jan", "Kowalski", "example@gm.pl", "Password123!", "1234", "9999");
//        List<Account> accounts = user.getAccounts();
//        assert !accounts.isEmpty();
    }
}