package banking_app.classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    @Override
    public boolean validate(String text) {
        Matcher matcher = emailPattern.matcher(text);
        return matcher.matches();
    }
}
