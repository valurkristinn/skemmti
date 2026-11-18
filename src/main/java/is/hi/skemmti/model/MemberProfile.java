package is.hi.skemmti.model;

import java.util.List;

/**
 * Profile of a member as other users see it. Only fields that are safe to
 * show to other users belong here.
 */
public record MemberProfile(
        Long id,
        String name,
        String description,
        String image) {

    public static MemberProfile from(User user) {
        return new MemberProfile(
                user.getUserId(),
                user.getUsername(),
                user.getDescription(),
                user.getImage());
    }

    public static List<MemberProfile> fromList(List<User> users) {
        return users.stream().map(MemberProfile::from).toList();
    }
}
