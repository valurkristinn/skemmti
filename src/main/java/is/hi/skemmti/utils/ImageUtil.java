package is.hi.skemmti.utils;

import is.hi.skemmti.exception.ApiException.InvalidData;

import java.util.Base64;
import java.util.Set;

public class ImageUtil {

    private static final int MAX_LENGTH = 2_000_000;
    private static final String BASE64_MARKER = ";base64,";
    private static final Set<String> ALLOWED_TYPES = Set.of("image/png", "image/jpeg");

    public static void validate(String image) {

        if (image == null || image.isEmpty()) {
            return;
        }

        if (image.length() > MAX_LENGTH) {
            throw new InvalidData("Image is too large");
        }

        if (!image.startsWith("data:") || !image.contains(BASE64_MARKER)) {
            throw new InvalidData("Image must be a base64 data URI");
        }

        int base64Index = image.indexOf(BASE64_MARKER);
        String type = image.substring(5, base64Index);
        if (!ALLOWED_TYPES.contains(type)) {
            throw new InvalidData("Only PNG and JPEG are allowed");
        }

        String base64Data = image.substring(base64Index + BASE64_MARKER.length());
        if (base64Data.isEmpty()) {
            throw new InvalidData("Image is empty");
        }
        try {
            Base64.getDecoder().decode(base64Data);
        } catch (IllegalArgumentException e) {
            throw new InvalidData("Image is not valid base64");
        }

    }
}