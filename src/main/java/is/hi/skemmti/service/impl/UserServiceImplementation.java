package is.hi.skemmti.service.impl;

import is.hi.skemmti.exception.ApiException.Conflict;
import is.hi.skemmti.exception.ApiException.NotFound;
import is.hi.skemmti.exception.ApiException.Forbidden;
import is.hi.skemmti.model.Event;
import is.hi.skemmti.model.Role;
import is.hi.skemmti.model.User;
import is.hi.skemmti.repository.UserRepository;
import is.hi.skemmti.service.UserService;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(String username, String password) {
        Optional<User> userSearch = userRepository.findByUsername(username);
        if (userSearch.isEmpty())
            throw new BadCredentialsException("User doesn't exist or password is incorrect");
        User user = userSearch.get();
        boolean doesPwMatch = passwordEncoder.matches(password, user.getPassword());

        if (!doesPwMatch)
            throw new BadCredentialsException("User doesn't exist or password is incorrect");

        return user;
    }

    @Override
    public User signup(String username, String password) {
        Optional<User> userSearch = userRepository.findByUsername(username);

        if (!userSearch.isEmpty())
            throw new Conflict("Username is already taken");

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.MEMBER);

        return userRepository.save(user);
    }

    @Override
    public User getUser(Long userId) {
        Optional<User> userSearch = userRepository.findById(userId);

        if (userSearch.isEmpty())
            throw new NotFound("User not found");

        return userSearch.get();
    }

    @Override
    @Transactional
    public void favoriteOrganizer(Long memberId, Long organizerId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new NotFound("User not found"));
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new NotFound("Organizer not found"));

        if (user.getRole() != Role.MEMBER) {
            throw new Forbidden("Only members can have favourites");
        }
        if (organizer.getRole() != Role.ORGANIZER) {
            throw new Forbidden("Only organizers can be favourited");
        }

        List<User> favourites = user.getFavoriteOrganizers();

        if (favourites.contains(organizer)) {
            throw new Conflict("This organizer is already a favourite");
        }

        favourites.add(organizer);
    }

    @Override
    @Transactional
    public void unfavoriteOrganizer(Long memberId, Long organizerId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new NotFound("User not found"));
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new NotFound("Organizer not found"));

        List<User> favourites = user.getFavoriteOrganizers();

        if (!favourites.remove(organizer)) {
            throw new NotFound("This organizer is not a favourite");
        }
    }

    @Override
    public List<User> getFavoriteOrganizers(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new NotFound("User not found"));
        return user.getFavoriteOrganizers();
    }

    @Override
    public List<Event> getAttendingEvents(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new NotFound("This user does not exist"));

        return user.getAttendingEvents();
    }
}
