package banking_exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String s) {
        super(s);
    }
}
