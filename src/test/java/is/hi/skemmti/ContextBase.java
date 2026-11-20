package is.hi.skemmti;

import is.hi.skemmti.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class ContextBase {

    protected static final String ADMIN = "admin";
    protected static final String ORGANIZER = "oktoberfest";
    protected static final String MEMBER = "jon.jonsson";

    @Autowired
    private UserRepository userRepository;

    // Looks up the id of a seeded user, so tests don't depend on fixed ids
    protected Long idOf(String username) {
        return userRepository.findByUsername(username).orElseThrow().getUserId();
    }
}
