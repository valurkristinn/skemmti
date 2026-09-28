package is.hi.skemmti.service.impl;

import is.hi.skemmti.exception.ApiException.Conflict;
import is.hi.skemmti.exception.ApiException.NotFound;
import is.hi.skemmti.model.Event;
import is.hi.skemmti.model.Role;
import is.hi.skemmti.model.User;
import is.hi.skemmti.repository.UserRepository;
import is.hi.skemmti.service.UserService;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public void favoriteOrganizer(Long memberId, Long organizerId) {
        // TODO: útfæra
    }

    @Override
    public void unfavoriteOrganizer(Long memberId, Long organizerId) {
        // TODO: útfæra
    }

    @Override
    public List<User> getFavoriteOrganizers(Long memberId) {
        // TODO: útfæra
        return null;
    }

    @Override
    public List<Event> getAttendingEvents(Long memberId) {
        // TODO: útfæra
        return null;
    }
}
