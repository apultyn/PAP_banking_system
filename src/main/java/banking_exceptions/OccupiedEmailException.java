package banking_exceptions;

public class OccupiedEmailException extends Exception {
    public OccupiedEmailException(String s) {
        super(s);
    }
}
