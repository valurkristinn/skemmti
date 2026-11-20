package is.hi.skemmti.controller;

import is.hi.skemmti.model.Event;
import is.hi.skemmti.model.EventProfile;
import is.hi.skemmti.model.MemberProfile;
import is.hi.skemmti.service.EventService;
import is.hi.skemmti.utils.General;
import is.hi.skemmti.utils.Validation;
import is.hi.skemmti.exception.ApiException.InvalidData;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
        public List<String> tags;
        public String image;
    }

    public record EventSearch(
            String searchTerm,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate afterDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime afterTime,
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime beforeTime) {
    }

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/event/{eventId}")
    public EventProfile getEvent(@PathVariable Long eventId) {
        return EventProfile.from(eventService.getEvent(eventId));
    }

    @GetMapping("/event")
    public List<EventProfile> getAllEvents(@RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit) {
        return EventProfile.fromList(General.paginate(eventService.getAllEvents(), offset, limit));
    }

    @GetMapping("/org/{orgId}/event")
    public List<EventProfile> getOrganizerEvents(@PathVariable Long orgId,
                                          @RequestParam(defaultValue = "all") String time,
                                          @RequestParam(required = false) Integer offset,
                                          @RequestParam(required = false) Integer limit) {

        if (!List.of("upcoming", "past", "all").contains(time))
            throw new InvalidData("time must be upcoming, past or all");
        
        return EventProfile.fromList(General.paginate(eventService.getOrganizerEvents(orgId, time), offset, limit));
    }

    // since EventSearch is a record it spreads each item as @RequestParam
    @GetMapping("/event/search")
    public List<EventProfile> searchEvents(EventSearch search,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit) {
        return EventProfile.fromList(General.paginate(eventService.searchEvents(search), offset, limit));
    }

    @PostMapping("/event")
    @ResponseStatus(HttpStatus.CREATED)
    public EventProfile createEvent(@AuthenticationPrincipal Jwt jwt, @RequestBody EventRequest req) {
        Event event = toEvent(req);
        Validation.validateEvent(event);

        Long organizerId = jwt.getClaim("uid");
        return EventProfile.from(eventService.createEvent(organizerId, event));
    }

    @PutMapping("/event/{eventId}")
    public EventProfile editEvent(@AuthenticationPrincipal Jwt jwt,
            @PathVariable Long eventId,
            @RequestBody EventRequest req) {

        Event updated = toEvent(req);
        Validation.validateEvent(updated);

        Long organizerId = jwt.getClaim("uid");
        return EventProfile.from(eventService.editEvent(organizerId, eventId, updated));
    }

    @PutMapping("/admin/event/{eventId}")
    public EventProfile adminEditEvent(@AuthenticationPrincipal Jwt jwt,
            @PathVariable Long eventId,
            @RequestBody EventRequest req) {

        Event updated = toEvent(req);
        Validation.validateEvent(updated);

        Long adminId = jwt.getClaim("uid");
        return EventProfile.from(eventService.editEvent(adminId, eventId, updated));
    }

    @DeleteMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable Long eventId) {
        Long organizerId = jwt.getClaim("uid");
        eventService.deleteOwnEvent(organizerId, eventId);
    }

    @PostMapping("/event/{eventId}/going")
    public void markGoing(@AuthenticationPrincipal Jwt jwt, @PathVariable Long eventId) {
        eventService.markGoing(jwt.getClaim("uid"), eventId);
    }

    @PostMapping("/event/{eventId}/notgoing")
    public void cancelGoing(@AuthenticationPrincipal Jwt jwt, @PathVariable Long eventId) {
        eventService.cancelGoing(jwt.getClaim("uid"), eventId);
    }

    @GetMapping("/event/{eventId}/going")
    public List<MemberProfile> getAttendees(@PathVariable Long eventId) {
        return MemberProfile.fromList(eventService.getAttendees(eventId));
    }

    @GetMapping("/event/{eventId}/going/count")
    public long getAttendeeCount(@PathVariable Long eventId) {
        return eventService.getAttendeeCount(eventId);
    }

    private Event toEvent(EventRequest req) {
        Event event = new Event();
        event.setName(req.name);
        event.setDate(req.date);
        event.setStartTime(req.startTime);
        event.setLocation(req.location);
        event.setDescription(req.description);
        event.setTags(req.tags);
        event.setImage(req.image);
        return event;
    }
}
