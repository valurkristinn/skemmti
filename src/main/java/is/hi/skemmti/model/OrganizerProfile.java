package is.hi.skemmti.model;

import java.util.List;

/**
 * Public profile of an organizer. Only fields that are safe to show to
 * anyone, logged in or not, belong here.
 */
public record OrganizerProfile(
        Long id,
        String name,
        String description,
        String image) {

    public static OrganizerProfile from(User user) {
        return new OrganizerProfile(
                user.getUserId(),
                user.getUsername(),
                user.getDescription(),
                user.getImage());
    }

    public static List<OrganizerProfile> fromList(List<User> users) {
        return users.stream().map(OrganizerProfile::from).toList();
    }
}