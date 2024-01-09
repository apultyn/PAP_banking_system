package banking_exceptions;

public class InvalidPinException extends Exception {
    public InvalidPinException(String s) {
        super(s);
    }
}
