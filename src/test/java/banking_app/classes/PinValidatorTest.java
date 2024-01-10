package banking_app.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PinValidatorTest {

    @Test
    void validate() {
        PinValidator pinV = new PinValidator();
        assert pinV.validate("1234");
    }

    @Test
    void validateEmpty() {
        PinValidator pinV = new PinValidator();
        assert !pinV.validate("");
    }

    @Test
    void validateTooShort() {
        PinValidator pinV = new PinValidator();
        assert !pinV.validate("123");
    }

    @Test
    void validateTooLong() {
        PinValidator pinV = new PinValidator();
        assert !pinV.validate("12345");
    }

    @Test
    void validateNotNumber() {
        PinValidator pinV = new PinValidator();
        assert !pinV.validate("12a4");
    }

}