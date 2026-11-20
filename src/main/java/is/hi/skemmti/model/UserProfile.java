package is.hi.skemmti.model;

/**
 * Profile of a user as they see it themselves, or as an admin sees it after
 * editing them. Includes the role, unlike MemberProfile.
 */
public record UserProfile(
        Long userId,
        String username,
        String image,
        String description,
        Role role) {

    public static UserProfile from(User user) {
        return new UserProfile(
                user.getUserId(),
                user.getUsername(),
                user.getImage(),
                user.getDescription(),
                user.getRole());
    }
}
