package banking_exceptions;

public class DepositNameExistingException extends Exception {
    public DepositNameExistingException(String s) {
        super(s);
    }
}
