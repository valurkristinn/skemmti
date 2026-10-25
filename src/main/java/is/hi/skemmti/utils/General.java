package is.hi.skemmti.utils;

import java.util.List;

public class General {

    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 20;

    public static <T> List<T> paginate(List<T> input, Integer offset, Integer limit) {
        int validatedOffset = DEFAULT_OFFSET;
        int validatedLimit = DEFAULT_LIMIT;

        if (offset != null) {
            Validation.validateInt(offset, "Offset", 0, Integer.MAX_VALUE);
            validatedOffset = offset;
        }
        if (limit != null) {
            Validation.validateInt(limit, "Limit", 0, 100);
            validatedLimit = limit;
        }

        return input.subList(Integer.min(validatedOffset, input.size()), Integer.min(validatedOffset + validatedLimit, input.size()));
    }

}
