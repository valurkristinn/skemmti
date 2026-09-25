package is.hi.skemmti.utils;

import is.hi.skemmti.exception.ApiException.InvalidData;

public class Validation {

    public static void validateLength(String input, String inputName, int min, int max) {
        if (input == null || input.length() < min)
            throw new InvalidData(inputName + " must be at least " + min + " characters");
        if (input.length() > max)
            throw new InvalidData(inputName + " cannot be longer than " + max + " characters");

    }
}
