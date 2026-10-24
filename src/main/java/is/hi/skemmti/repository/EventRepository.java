package is.hi.skemmti.repository;

import is.hi.skemmti.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findById(Long eventId);

    List<Event> findAll();

    List<Event> findByPublishedTrue();

    List<Event> findByDate(LocalDate date);

    List<Event> findByOrganizer_UserId(Long organizerId);

    @Query("""
            SELECT e FROM Event e LEFT JOIN e.organizer o
            WHERE e.published = true
              AND (:pattern IS NULL
                    OR LOWER(e.name) LIKE :pattern
                    OR LOWER(e.description) LIKE :pattern
                    OR LOWER(array_to_string(e.tags, ' ')) LIKE :pattern
                    OR LOWER(o.username) LIKE :pattern
                    OR LOWER(e.location) LIKE :pattern)
              AND (:beforeDate IS NULL OR e.date <= :beforeDate)
              AND (:afterDate IS NULL OR e.date >= :afterDate)
              AND (:beforeTime IS NULL OR e.startTime <= :beforeTime)
              AND (:afterTime IS NULL OR e.startTime >= :afterTime)
            ORDER BY CASE
                    WHEN LOWER(e.name) LIKE :pattern THEN 0
                    WHEN LOWER(o.username) LIKE :pattern THEN 1
                    WHEN LOWER(array_to_string(e.tags, ' ')) LIKE :pattern THEN 2
                    ELSE 3
                END, e.date, e.startTime
            """)
    List<Event> search(String pattern, LocalDate beforeDate, LocalDate afterDate,
            LocalTime beforeTime, LocalTime afterTime);

    Event save(Event event);

    void delete(Event event);
}
