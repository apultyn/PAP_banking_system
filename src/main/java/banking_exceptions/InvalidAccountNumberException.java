package banking_exceptions;

public class InvalidAccountNumberException extends Exception {
    public InvalidAccountNumberException(String s) {
        super(s);
    }
}
