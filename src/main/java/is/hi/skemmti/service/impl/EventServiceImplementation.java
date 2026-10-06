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

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new NotFound("User not found"));

        if (organizer.getRole() != Role.ORGANIZER)
            throw new Forbidden("Only organizers can publish events");

        // Validation.validateLength(event.getName(), "Name", 1, 100);
        // Validation.validateLength(event.getLocation(), "Location", 1, 100);
        // if (event.getDate() == null)
        //     throw new InvalidData("Date is required");
        // if (event.getStartTime() == null)
        //     throw new InvalidData("Start time is required");
        // tók út því validation fer fram í controller

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
        existing.setTag(updated.getTag());
        existing.setImage(updated.getImage());
        // open issue: ætti edit að breyta event id?

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
