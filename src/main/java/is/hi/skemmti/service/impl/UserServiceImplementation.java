package is.hi.skemmti.service.impl;

import is.hi.skemmti.model.Event;
import is.hi.skemmti.model.User;
import is.hi.skemmti.repository.UserRepository;
import is.hi.skemmti.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        // TODO: útfæra
        return null;
    }

    @Override
    public User signup(String username, String password) {
        // TODO: útfæra
        return null;
    }

    @Override
    public User getUser(Long userId) {
        // TODO: útfæra
        return null;
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
