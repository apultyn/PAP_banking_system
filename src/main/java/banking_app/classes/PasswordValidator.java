package banking_app.classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements Validator {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,20}$";

    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public boolean validate(String text) {
        Matcher matcher = passwordPattern.matcher(text);
        return matcher.matches();
    }

}
