package is.hi.skemmti.service;

import is.hi.skemmti.exception.ApiException.Forbidden;
import is.hi.skemmti.exception.ApiException.NotFound;
import is.hi.skemmti.repository.EventRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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

// Uses the users and events seeded in data.sql, each test is rolled back
@SpringBootTest
@Transactional
class UserServiceTests {

    private static final Long ADMIN_ID = 1L;
    private static final Long ORGANIZER_ID = 2L;
    private static final Long MEMBER_ID = 3L;

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
        userService.deleteUser(MEMBER_ID, MEMBER_ID);

        assertThrows(NotFound.class, () -> userService.getUser(MEMBER_ID));
    }

    @Test
    void orgDeletesThemselves() {
        userService.deleteUser(ORGANIZER_ID, ORGANIZER_ID);

        assertThrows(NotFound.class, () -> userService.getUser(ORGANIZER_ID));
        assertTrue(eventRepository.findByOrganizer_UserId(ORGANIZER_ID).isEmpty());
        assertTrue(userService.getAttendingEvents(MEMBER_ID).isEmpty());
        assertTrue(userService.getFavoriteOrganizers(MEMBER_ID).isEmpty());
    }

    @Test
    void adminDeletesThemselves() {
        assertThrows(Forbidden.class, () -> userService.deleteUser(ADMIN_ID, ADMIN_ID));

        assertDoesNotThrow(() -> userService.getUser(ADMIN_ID));
    }

    @Test
    void adminDeletesMember() {
        userService.deleteUser(ADMIN_ID, MEMBER_ID);

        assertThrows(NotFound.class, () -> userService.getUser(MEMBER_ID));
    }

    @Test
    void memberEditsOwnDescriptionAndRemovesImage() {
        EditUserRequest req = new EditUserRequest();
        req.description = "Ný lýsing";
        req.image = "";

        User updated = userService.editUser(MEMBER_ID, MEMBER_ID, req);

        assertEquals("Ný lýsing", updated.getDescription());
        assertNull(updated.getImage());
        assertEquals("Jon Jonsson", updated.getUsername()); // null = unchanged
    }

    @Test
    void memberCannotEditSomeoneElse() {
        EditUserRequest req = new EditUserRequest();
        req.description = "hakk";

        assertThrows(Forbidden.class, () -> userService.editUser(MEMBER_ID, ORGANIZER_ID, req));
    }

    @Test
    void usernameTakenReturnsConflict() {
        EditUserRequest req = new EditUserRequest();
        req.username = "Oktoberfest";

        assertThrows(Conflict.class, () -> userService.editUser(MEMBER_ID, MEMBER_ID, req));
    }

    @Test
    void passwordChangeRequiresCurrentPassword() {
        User member = userService.getUser(MEMBER_ID);
        member.setPassword(passwordEncoder.encode("oldpassword"));
        userRepository.save(member);

        EditUserRequest wrong = new EditUserRequest();
        wrong.password = "newpassword";
        wrong.currentPassword = "notmypassword";
        assertThrows(Forbidden.class, () -> userService.editUser(MEMBER_ID, MEMBER_ID, wrong));

        EditUserRequest right = new EditUserRequest();
        right.password = "newpassword";
        right.currentPassword = "oldpassword";
        userService.editUser(MEMBER_ID, MEMBER_ID, right);

        assertDoesNotThrow(() -> userService.login("Jon Jonsson", "newpassword"));
    }

    @Test
    void adminEditsMemberButNotPassword() {
        EditUserRequest req = new EditUserRequest();
        req.description = "Breytt af admin";
        userService.editUser(ADMIN_ID, MEMBER_ID, req);
        assertEquals("Breytt af admin", userService.getUser(MEMBER_ID).getDescription());

        EditUserRequest pw = new EditUserRequest();
        pw.password = "newpassword";
        assertThrows(Forbidden.class, () -> userService.editUser(ADMIN_ID, MEMBER_ID, pw));
    }
}
