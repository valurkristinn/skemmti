package is.hi.skemmti.controller;

import is.hi.skemmti.model.Event;
import is.hi.skemmti.model.User;
import is.hi.skemmti.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class EventController {

    private final EventService eventService;

    public static class EventRequest {
        public String name;
        public LocalDate date;
        public LocalTime startTime;
        public String location;
        public String description;
        public String tag;
        public String image;
    }

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // TODO: bæta við @GetMapping/@PostMapping/@PutMapping/@DeleteMapping o.fl. þegar endapunktar eru ákveðnir

    public Event getEvent(Long eventId) {
        // TODO: útfæra
        return null;
    }

    @GetMapping("/event")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    public List<Event> searchEventsByDate(LocalDate date) {
        // TODO: útfæra
        return null;
    }

    public List<Event> searchEventsByOrganizer(Long organizerId) {
        // TODO: útfæra
        return null;
    }

    @PostMapping("/event/publish")
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@AuthenticationPrincipal Jwt jwt, @RequestBody EventRequest req) {
        Event event = new Event();
        event.setName(req.name);
        event.setDate(req.date);
        event.setStartTime(req.startTime);
        event.setLocation(req.location);
        event.setDescription(req.description);
        event.setTag(req.tag);
        event.setImage(req.image);

        Long organizerId = jwt.getClaim("uid");
        return eventService.createEvent(organizerId, event);
    }

    public Event editEvent(Long organizerId, Long eventId, Event event) {
        // TODO: útfæra
        return null;
    }

    public void deleteEvent(Long adminId, Long eventId) {
        // TODO: útfæra
    }

    @DeleteMapping("/event/delete/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable Long eventId) {
        Long organizerId = jwt.getClaim("uid");
        eventService.deleteOwnEvent(organizerId, eventId);
    }

    public void markGoing(Long memberId, Long eventId) {
        // TODO: útfæra
    }

    public void cancelGoing(Long memberId, Long eventId) {
        // TODO: útfæra
    }

    public List<User> getAttendingMembers(Long eventId) {
        // TODO: útfæra
        return null;
    }

    public void publish(Long userId, Long eventId) {
        // TODO: útfæra
    }

    public void unpublish(Long userId, Long eventId) {
        // TODO: útfæra
    }
}
