package is.hi.skemmti.utils;

import java.util.List;
import is.hi.skemmti.controller.UserController.EditUserRequest;

import is.hi.skemmti.exception.ApiException.InvalidData;
import is.hi.skemmti.model.Event;

public class Validation {

    private static final int MAX_TAGS = 20;
    private static final String USERNAME_PATTERN = "[a-z0-9._-]+";
    private static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[A-Z])\\S+";

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

    public static void validateUsername(String username) {
        validateLength(username, "Username", 3, 32);

        if (!username.matches(USERNAME_PATTERN))
            throw new InvalidData("Username may only contain lowercase letters, numbers, '.', '_' and '-'");
    }

    public static void validatePassword(String password) {
        validateLength(password, "Password", 8, 32);

        if (!password.matches(PASSWORD_PATTERN))
            throw new InvalidData("Password must contain a number and a capital letter, and no spaces");
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

    public static void validateUserEdit(EditUserRequest req) {
        if (req.username != null)
            validateUsername(req.username);
        if (req.description != null)
            validateLength(req.description, "Description", 0, 255);
        if (req.password != null)
            validatePassword(req.password);

        ImageUtil.validate(req.image);
    }
}
