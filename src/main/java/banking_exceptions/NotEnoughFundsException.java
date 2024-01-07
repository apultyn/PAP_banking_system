package banking_exceptions;

public class NotEnoughFundsException extends Exception {
    public NotEnoughFundsException(String s) {
        super(s);
    }
}
