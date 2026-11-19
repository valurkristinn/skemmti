package is.hi.skemmti.utils;

import is.hi.skemmti.exception.ApiException.InvalidData;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationUtilityTests {

    private static final String VALID_USERNAME = "jon.jonsson_1-a";
    private static final String UPPERCASE_USERNAME = "Jon.jonsson";
    private static final String USERNAME_WITH_SPACE = "jon jonsson";
    private static final String USERNAME_WITH_SYMBOL = "jon@jonsson";
    private static final String USERNAME_WITH_ACCENT = "jón.jonsson";

    private static final String VALID_PASSWORD = "Password123";
    private static final String PASSWORD_WITHOUT_NUMBER = "Password";
    private static final String PASSWORD_WITHOUT_CAPITAL = "password123";
    private static final String PASSWORD_WITH_SPACE = "Pass word123";
    private static final String PASSWORD_WITH_ICELANDIC_CAPITAL = "Ápassword123";

    @Test
    void validUsernameIsAccepted() {
        assertDoesNotThrow(() -> Validation.validateUsername(VALID_USERNAME));
    }

    @Test
    void usernameWithUppercaseIsRejected() {
        assertThrows(InvalidData.class, () -> Validation.validateUsername(UPPERCASE_USERNAME));
    }

    @Test
    void usernameWithSpaceIsRejected() {
        assertThrows(InvalidData.class, () -> Validation.validateUsername(USERNAME_WITH_SPACE));
    }

    @Test
    void usernameWithSymbolIsRejected() {
        assertThrows(InvalidData.class, () -> Validation.validateUsername(USERNAME_WITH_SYMBOL));
    }

    @Test
    void usernameWithAccentIsRejected() {
        assertThrows(InvalidData.class, () -> Validation.validateUsername(USERNAME_WITH_ACCENT));
    }

    @Test
    void validPasswordIsAccepted() {
        assertDoesNotThrow(() -> Validation.validatePassword(VALID_PASSWORD));
    }

    @Test
    void passwordWithoutNumberIsRejected() {
        assertThrows(InvalidData.class, () -> Validation.validatePassword(PASSWORD_WITHOUT_NUMBER));
    }

    @Test
    void passwordWithoutCapitalIsRejected() {
        assertThrows(InvalidData.class, () -> Validation.validatePassword(PASSWORD_WITHOUT_CAPITAL));
    }

    @Test
    void passwordWithSpaceIsRejected() {
        assertThrows(InvalidData.class, () -> Validation.validatePassword(PASSWORD_WITH_SPACE));
    }

    @Test
    void icelandicCapitalDoesNotCountAsCapital() {
        assertThrows(InvalidData.class, () -> Validation.validatePassword(PASSWORD_WITH_ICELANDIC_CAPITAL));
    }
}
