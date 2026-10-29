package is.hi.skemmti.service.impl;

import is.hi.skemmti.exception.ApiException.Forbidden;
import is.hi.skemmti.exception.ApiException.NotFound;
import is.hi.skemmti.controller.EventController.EventSearch;
import is.hi.skemmti.exception.ApiException.Conflict;
import is.hi.skemmti.model.Event;
import is.hi.skemmti.model.Role;
import is.hi.skemmti.model.User;
import is.hi.skemmti.repository.EventRepository;
import is.hi.skemmti.repository.UserRepository;
import is.hi.skemmti.service.EventService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImplementation implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventServiceImplementation(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
            .filter(Event::isPublished)
            .orElseThrow(() -> new NotFound("Event not found"));
    }

    @Override
    public User getEventOrganizer(Long eventId) {
        Event event = getEvent(eventId);
        User organizer = event.getOrganizer();

        if (organizer == null)
            throw new NotFound("Organizer not found");

        return organizer;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findByPublishedTrue();
    }

    @Override
    public List<Event> searchEvents(EventSearch e) {
        return eventRepository.search(toLikePattern(e.searchTerm()), e.beforeDate(), e.afterDate(),
                e.beforeTime(), e.afterTime());
    }

    private static String toLikePattern(String term) {
        return term == null ? null : "%" + term.toLowerCase() + "%";
    }

    @Override
    public Event createEvent(Long organizerId, Event event) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new NotFound("User not found"));

        if (organizer.getRole() != Role.ORGANIZER)
            throw new Forbidden("Only organizers can publish events");

        event.setEventId(null);
        event.setOrganizer(organizer);
        event.setPublished(true);

        return eventRepository.save(event);
    }

    @Override
    public Event editEvent(Long organizerId, Long eventId, Event updated) {
        Event existing = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFound("Event not found"));

        if (!existing.getOrganizer().getUserId().equals(organizerId))
            throw new Forbidden("You can only edit your own events");

        existing.setName(updated.getName());
        existing.setDate(updated.getDate());
        existing.setStartTime(updated.getStartTime());
        existing.setLocation(updated.getLocation());
        existing.setDescription(updated.getDescription());
        existing.setTags(updated.getTags());
        existing.setImage(updated.getImage());

        return eventRepository.save(existing);
    }

    @Override
    public void deleteEvent(Long adminId, Long eventId) {
        // TODO: útfæra - athuga að adminId hafi Role.ADMIN
    }

    @Override
    @Transactional
    public void deleteOwnEvent(Long organizerId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFound("Event not found"));

        if (!event.getOrganizer().getUserId().equals(organizerId))
            throw new Forbidden("You can only delete your own events");

        for (User attendee : userRepository.findByAttendingEvents_EventId(eventId)) {
            attendee.getAttendingEvents().remove(event);
        }

        eventRepository.delete(event);
    }

    @Override
    @Transactional
    public void markGoing(Long memberId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFound("Event not found"));
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new NotFound("User not found"));

        if (user.getRole() != Role.MEMBER) {
            throw new Forbidden("Only members can set status to going");
        }

        List<Event> going = user.getAttendingEvents();

        if (going.contains(event)) {
            throw new Conflict("User already marked as going");
        }

        going.add(event);
    }

    @Override
    @Transactional
    public void cancelGoing(Long memberId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFound("Event not found"));
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new NotFound("User not found"));

        List<Event> going = user.getAttendingEvents();

        if (!going.remove(event)) {
            throw new NotFound("User is not marked as going");
        }
    }

    @Override
    public List<User> getAttendees(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFound("Event not found"));

        return userRepository.findByAttendingEvents_EventId(eventId);
    }

    @Override
    public long getAttendeeCount(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFound("Event not found"));

        return userRepository.countByAttendingEvents_EventId(eventId);
    }

    @Override
    public void publish(Long userId, Long eventId) {
        // TODO: útfæra - athuga að userId sé skipuleggjandi viðburðarins áður en birt
        // er
    }

    @Override
    public void unpublish(Long userId, Long eventId) {
        // TODO: útfæra - athuga að userId sé skipuleggjandi viðburðarins áður en falið
        // er
    }
}
