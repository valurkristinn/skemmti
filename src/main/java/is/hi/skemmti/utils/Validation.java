package is.hi.skemmti.utils;

import java.util.List;

import is.hi.skemmti.exception.ApiException.InvalidData;
import is.hi.skemmti.model.Event;

public class Validation {

    private static final int MAX_TAGS = 20;

    public static void validateLength(String input, String label, int min, int max) {
        notNull(input, label);

        if (input.length() < min)
            throw new InvalidData(label + " must be atleast " + min + " characters");
        if (input.length() > max)
            throw new InvalidData(label + " must be " + max + " or fewer characters");

    }

    public static void validateInt(int input, String label, int min, int max) {
        if (input < min)
            throw new InvalidData(label + " must atleast " + min);
        if (input > max)
            throw new InvalidData(label + " must be " + max + " or less");

    }

    public static void notNull(Object input, String label) {
        if (input == null) {
            throw new InvalidData(label + " is required");
        }
    }

    public static void validateEvent(Event event) {
        validateLength(event.getName(), "Name", 1, 100);
        validateLength(event.getLocation(), "Location", 1, 100);
        validateLength(event.getDescription(), "Description", 0, 255);

        notNull(event.getDate(), "Date");

        notNull(event.getStartTime(), "Start time");

        List<String> tags = event.getTags();

        if (tags != null) {
            if (tags.size() > MAX_TAGS) {
                throw new InvalidData("Only " + MAX_TAGS + " tags are allowed");
            }
            for (String t : tags) {
                validateLength(t, "Tags", 1, 15);
            }
        }

        ImageUtil.validate(event.getImage());
    }
}
