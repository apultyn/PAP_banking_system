package banking_app.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    @Test
    void validate() {
        EmailValidator emailV = new EmailValidator();
        assert emailV.validate("example@op.pl");
    }

    @Test
    void validateEmpty() {
        EmailValidator emailV = new EmailValidator();
        assert !emailV.validate("");
    }

    @Test
    void validateNoAt() {
        EmailValidator emailV = new EmailValidator();
        assert !emailV.validate("exampleop.pl");
    }

    @Test
    void validateNoDot() {
        EmailValidator emailV = new EmailValidator();
        assert !emailV.validate("example@oppl");
    }

    @Test
    void validateTwoDots() {
        EmailValidator emailV = new EmailValidator();
        assert !emailV.validate("example@op..pl");
    }

    @Test
    void validateNumberAfterDot() {
        EmailValidator emailV = new EmailValidator();
        assert !emailV.validate("example@op.pl2");
    }
}