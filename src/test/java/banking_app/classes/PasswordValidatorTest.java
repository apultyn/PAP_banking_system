package banking_app.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    void validate() {
        PasswordValidator passwordV = new PasswordValidator();
        assert passwordV.validate("Password123!");
    }

    @Test
    void validateEmpty() {
        PasswordValidator passwordV = new PasswordValidator();
        assert !passwordV.validate("");
    }

    @Test
    void validateTooShort() {
        PasswordValidator passwordV = new PasswordValidator();
        assert !passwordV.validate("Pas12!");
    }

    @Test
    void validateNoNumber() {
        PasswordValidator passwordV = new PasswordValidator();
        assert !passwordV.validate("Password!");
    }

    @Test
    void validateNoSpecialChar() {
        PasswordValidator passwordV = new PasswordValidator();
        assert !passwordV.validate("Password1");
    }

    @Test
    void validateNoBigLetter() {
        PasswordValidator passwordV = new PasswordValidator();
        assert !passwordV.validate("password12$");
    }
}