package is.hi.skemmti.repository;

import is.hi.skemmti.model.Role;
import is.hi.skemmti.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // findById(Long), save(User) og delete(User) koma nú þegar frá JpaRepository,
    // en eru sett hér inn til að passa við diagram-ið.
    Optional<User> findById(Long userId);

    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);

    User save(User user);

    void delete(User user);
}
