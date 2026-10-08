package is.hi.skemmti.utils;

import is.hi.skemmti.exception.ApiException.InvalidData;

import java.util.Base64;
import java.util.Set;

public class ImageUtil {

    private static final int MAX_LENGTH = 2_000_000;
    private static final String BASE64_MARKER = ";base64,";
    private static final Set<String> ALLOWED_TYPES = Set.of("image/png", "image/jpeg");

    public static void validate(String image) {
        // TODO 1: ef image er null, þá er engin mynd og ekkert að athuga: hætta (return)
        if (image == null || image.isEmpty()) {
            return;
        }

        // TODO 2: ef image.length() > MAX_LENGTH, kasta InvalidData("Image is too large")
        if (image.length() > MAX_LENGTH) {
            throw new InvalidData("Image is too large");
        }

        // TODO 3: ef image byrjar ekki á "data:" eða inniheldur ekki ";base64,",
        //         kasta InvalidData("Image must be a base64 data URI")
        if (!image.startsWith("data:") || !image.contains(BASE64_MARKER)) {
            throw new InvalidData("Image must be a base64 data URI");
        }

        // TODO 4: finna hvar ";base64," byrjar og taka út gerðina
        //         (strenginn á milli "data:" og ";base64,"), t.d. "image/png"
        //         ef gerðin er ekki í ALLOWED_TYPES, kasta InvalidData("Only PNG and JPEG are allowed")
        int base64Index = image.indexOf(BASE64_MARKER);
        String type = image.substring(5, base64Index);
        if (!ALLOWED_TYPES.contains(type)) {
            throw new InvalidData("Only PNG and JPEG are allowed");
        }

        // TODO 5: taka gögnin (allt á eftir ";base64,") og afkóða þau með
        //         Base64.getDecoder().decode(...)
        //         ef það kastar IllegalArgumentException, kasta InvalidData("Image is not valid base64")
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