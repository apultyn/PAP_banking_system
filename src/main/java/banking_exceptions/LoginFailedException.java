package banking_exceptions;

public class LoginFailedException extends Exception {
    public LoginFailedException(String s) {
        super(s);
    }
}
