package is.hi.skemmti.model;

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