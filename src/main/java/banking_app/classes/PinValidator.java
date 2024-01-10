package banking_app.classes;

public class PinValidator implements Validator {
    private final String regex = "\\d{4}";
    @Override
    public boolean validate(String pin) {
        return pin != null && pin.matches(regex);
    }
}
