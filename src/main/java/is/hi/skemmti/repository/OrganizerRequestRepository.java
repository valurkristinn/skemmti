package is.hi.skemmti.repository;

import is.hi.skemmti.model.OrganizerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRequestRepository extends JpaRepository<OrganizerRequest, Long> {

    boolean existsByUsername(String username);
}