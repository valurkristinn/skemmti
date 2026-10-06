package is.hi.skemmti.utils;

import is.hi.skemmti.exception.ApiException.InvalidData;


public class Validation {

    public static void validateLength(String input, String inputName, int min, int max) {
        if (input == null || input.length() < min)
            throw new InvalidData(inputName + " must be at least " + min + " characters");
        if (input.length() > max)
            throw new InvalidData(inputName + " cannot be longer than " + max + " characters");

    }

    public static void validateEvent(Event event) {
        Validation.validateLength(event.getName(), "Name", 1, 100);
        Validation.validateLength(event.getLocation(), "Location", 1, 100);
        if (event.getDate() == null)
            throw new InvalidData("Date is required");
        if (event.getStartTime() == null)
            throw new InvalidData("Start time is required");
    }
}
