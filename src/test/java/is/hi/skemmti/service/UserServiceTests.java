package is.hi.skemmti.service;

import is.hi.skemmti.exception.ApiException.Forbidden;
import is.hi.skemmti.exception.ApiException.NotFound;
import is.hi.skemmti.repository.EventRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import is.hi.skemmti.controller.UserController.EditUserRequest;
import is.hi.skemmti.exception.ApiException.Conflict;
import is.hi.skemmti.model.User;
import is.hi.skemmti.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTests extends BaseServiceTests {

    private static final String OLD_PASSWORD = "oldpassword";
    private static final String NEW_PASSWORD = "newpassword";
    private static final String WRONG_PASSWORD = "notmypassword";
    private static final String NEW_DESCRIPTION = "Ný lýsing";
    private static final String ADMIN_DESCRIPTION = "Breytt af admin";

    @Autowired
    private UserService userService;

    // EventService hides unpublished events, so the repository is used to check they are gone
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void memberDeletesThemselves() {
        userService.deleteUser(memberId, memberId);

        assertThrows(NotFound.class, () -> userService.getUser(memberId));
    }

    @Test
    void orgDeletesThemselves() {
        userService.deleteUser(organizerId, organizerId);

        assertThrows(NotFound.class, () -> userService.getUser(organizerId));
        assertTrue(eventRepository.findByOrganizer_UserId(organizerId).isEmpty());
        assertTrue(userService.getAttendingEvents(memberId).isEmpty());
        assertTrue(userService.getFavoriteOrganizers(memberId).isEmpty());
    }

    @Test
    void adminDeletesThemselves() {
        assertThrows(Forbidden.class, () -> userService.deleteUser(adminId, adminId));

        assertDoesNotThrow(() -> userService.getUser(adminId));
    }

    @Test
    void adminDeletesMember() {
        userService.deleteUser(adminId, memberId);

        assertThrows(NotFound.class, () -> userService.getUser(memberId));
    }

    @Test
    void memberEditsOwnDescriptionAndRemovesImage() {
        EditUserRequest req = new EditUserRequest();
        req.description = NEW_DESCRIPTION;
        req.image = "";

        User updated = userService.editUser(memberId, memberId, req);

        assertEquals(NEW_DESCRIPTION, updated.getDescription());
        assertNull(updated.getImage());
        assertEquals(MEMBER, updated.getUsername()); // null = unchanged
    }

    @Test
    void memberCannotEditSomeoneElse() {
        EditUserRequest req = new EditUserRequest();
        req.description = "hakk";

        assertThrows(Forbidden.class, () -> userService.editUser(memberId, organizerId, req));
    }

    @Test
    void usernameTakenReturnsConflict() {
        EditUserRequest req = new EditUserRequest();
        req.username = ORGANIZER;

        assertThrows(Conflict.class, () -> userService.editUser(memberId, memberId, req));
    }

    @Test
    void passwordChangeRequiresCurrentPassword() {
        User member = userService.getUser(memberId);
        member.setPassword(passwordEncoder.encode(OLD_PASSWORD));
        userRepository.save(member);

        EditUserRequest wrong = new EditUserRequest();
        wrong.password = NEW_PASSWORD;
        wrong.currentPassword = WRONG_PASSWORD;
        assertThrows(Forbidden.class, () -> userService.editUser(memberId, memberId, wrong));

        EditUserRequest right = new EditUserRequest();
        right.password = NEW_PASSWORD;
        right.currentPassword = OLD_PASSWORD;
        userService.editUser(memberId, memberId, right);

        assertDoesNotThrow(() -> userService.login(MEMBER, NEW_PASSWORD));
    }

    @Test
    void adminEditsMemberButNotPassword() {
        EditUserRequest req = new EditUserRequest();
        req.description = ADMIN_DESCRIPTION;
        userService.editUser(adminId, memberId, req);
        assertEquals(ADMIN_DESCRIPTION, userService.getUser(memberId).getDescription());

        EditUserRequest pw = new EditUserRequest();
        pw.password = NEW_PASSWORD;
        assertThrows(Forbidden.class, () -> userService.editUser(adminId, memberId, pw));
    }
}
