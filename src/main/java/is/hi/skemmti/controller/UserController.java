package is.hi.skemmti.controller;

import is.hi.skemmti.model.Event;
import is.hi.skemmti.model.User;
import is.hi.skemmti.service.UserService;
import is.hi.skemmti.utils.Validation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtEncoder jwtEncoder;

    private static final int TOKEN_LIFETIME = 1209600; // 60*60*24*14=1209600, 14 days

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public UserController(UserService userService, JwtEncoder jwtEncoder) {
        this.userService = userService;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        User user = userService.login(req.username, req.password);
        return createToken(user);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody LoginRequest req) {
        Validation.validateLength(req.password, "Password", 8, 32);
        Validation.validateLength(req.username, "Username", 3, 32);

        User user = userService.signup(req.username, req.password);
        return createToken(user);
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    // EXAMPLE: How to get currently logged in user - TBD if this should be kept 
    @GetMapping("/user")
    public User getUser(@AuthenticationPrincipal Jwt jwt) {
        return userService.getUser(jwt.getClaim("uid"));
    }

    public void favoriteOrganizer(Long memberId, Long organizerId) {
        // TODO: útfæra
    }

    public void unfavoriteOrganizer(Long memberId, Long organizerId) {
        // TODO: útfæra
    }

    public List<User> getFavoriteOrganizers(Long memberId) {
        // TODO: útfæra
        return null;
    }

    public List<Event> getAttendingEvents(Long memberId) {
        // TODO: útfæra
        return null;
    }

    private String createToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .claim("uid", user.getUserId())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(TOKEN_LIFETIME))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
