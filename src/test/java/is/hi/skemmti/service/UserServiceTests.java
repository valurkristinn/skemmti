package is.hi.skemmti.service;

import is.hi.skemmti.exception.ApiException.Forbidden;
import is.hi.skemmti.exception.ApiException.NotFound;
import is.hi.skemmti.repository.EventRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
}
