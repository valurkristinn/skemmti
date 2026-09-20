package is.hi.skemmti.service;

import is.hi.skemmti.model.Event;
import is.hi.skemmti.model.User;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    Event getEvent(Long eventId);

    List<Event> getAllEvents();

    List<Event> searchEventsByDate(LocalDate date);

    List<Event> searchEventsByOrganizer(Long organizerId);

    Event createEvent(Long organizerId, Event event);

    Event editEvent(Long organizerId, Long eventId, Event event);

    void deleteEvent(Long adminId, Long eventId);

    void deleteOwnEvent(Long organizerId, Long eventId);

    void markGoing(Long memberId, Long eventId);

    void cancelGoing(Long memberId, Long eventId);

    List<User> getAttendingMembers(Long eventId);

    void publish(Long userId, Long eventId);

    void unpublish(Long userId, Long eventId);
}
