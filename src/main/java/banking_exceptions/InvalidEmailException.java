package banking_exceptions;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String s) {
        super(s);
    }
}
