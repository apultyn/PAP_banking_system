package banking_exceptions;

public class InvalidAmountException extends Exception {
    public InvalidAmountException(String s) {
        super(s);
    }
}
