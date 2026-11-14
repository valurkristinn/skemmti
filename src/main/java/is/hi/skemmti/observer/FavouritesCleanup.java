package is.hi.skemmti.observer;

import java.util.List;

import org.springframework.stereotype.Component;

import is.hi.skemmti.model.User;
import is.hi.skemmti.repository.UserRepository;
import jakarta.transaction.Transactional;

@Component 
class FavouritesCleanup implements Observer {

    private final UserRepository userRepository;

    public FavouritesCleanup(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void handleEvent(User user) {
        List<User> hasFavourite = userRepository.findByFavoriteOrganizers_UserId(user.getUserId());

        for (User f : hasFavourite) {
            f.getFavoriteOrganizers().remove(user);
        }

    }
}
