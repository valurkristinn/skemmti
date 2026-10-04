package is.hi.skemmti.service.impl;

import is.hi.skemmti.exception.ApiException.Forbidden;
import is.hi.skemmti.exception.ApiException.InvalidData;
import is.hi.skemmti.exception.ApiException.NotFound;
import is.hi.skemmti.model.Event;
import is.hi.skemmti.model.Role;
import is.hi.skemmti.model.User;
import is.hi.skemmti.repository.EventRepository;
import is.hi.skemmti.repository.UserRepository;
import is.hi.skemmti.service.EventService;
import is.hi.skemmti.utils.Validation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        // TODO: útfæra
        return null;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> searchEventsByDate(LocalDate date) {
        // TODO: útfæra
        return null;
    }

    @Override
    public List<Event> searchEventsByOrganizer(Long organizerId) {
        // TODO: útfæra
        return null;
    }

    @Override
    public Event createEvent(Long organizerId, Event event) {
        // 1. Find the user who is making the request
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new NotFound("User not found"));

        // 2. Only organizers can create events
        if (organizer.getRole() != Role.ORGANIZER)
            throw new Forbidden("Only organizers can publish events");

        // 3. Validate the event data
        Validation.validateLength(event.getName(), "Name", 1, 100);
        Validation.validateLength(event.getLocation(), "Location", 1, 100);
        if (event.getDate() == null)
            throw new InvalidData("Date is required");
        if (event.getStartTime() == null)
            throw new InvalidData("Start time is required");

        // 4. Fill in what the server controls
        event.setEventId(null);
        event.setOrganizer(organizer);
        event.setPublished(true);

        // 5. Save and return the new event
        return eventRepository.save(event);
    }

    @Override
    public Event editEvent(Long organizerId, Long eventId, Event event) {
        // TODO: útfæra - athuga að viðburðurinn tilheyri þessum skipuleggjanda
        return null;
    }

    @Override
    public void deleteEvent(Long adminId, Long eventId) {
        // TODO: útfæra - athuga að adminId hafi Role.ADMIN
    }

    @Override
    @Transactional
    public void deleteOwnEvent(Long organizerId, Long eventId) {
        // 1. Find the event
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFound("Event not found"));

        // 2. Only the event's owner can delete it
        if (!event.getOrganizer().getUserId().equals(organizerId))
            throw new Forbidden("You can only delete your own events");

        // 3. Remove the event from everyone attending it
        for (User attendee : userRepository.findByAttendingEvents_EventId(eventId)) {
            attendee.getAttendingEvents().remove(event);
        }

        // 4. Delete the event
        eventRepository.delete(event);
    }

    @Override
    public void markGoing(Long memberId, Long eventId) {
        // TODO: útfæra
    }

    @Override
    public void cancelGoing(Long memberId, Long eventId) {
        // TODO: útfæra
    }

    @Override
    public List<User> getAttendingMembers(Long eventId) {
        // TODO: útfæra
        return null;
    }

    @Override
    public void publish(Long userId, Long eventId) {
        // TODO: útfæra - athuga að userId sé skipuleggjandi viðburðarins áður en birt er
    }

    @Override
    public void unpublish(Long userId, Long eventId) {
        // TODO: útfæra - athuga að userId sé skipuleggjandi viðburðarins áður en falið er
    }
}
