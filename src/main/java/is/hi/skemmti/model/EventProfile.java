package is.hi.skemmti.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public record EventProfile(
        Long eventId,
        String name,
        LocalDate date,
        LocalTime startTime,
        String location,
        String description,
        List<String> tags,
        String image,
        boolean published,
        OrganizerProfile organizer) {

    public static EventProfile from(Event event) {
        User organizer = event.getOrganizer();
        return new EventProfile(
                event.getEventId(),
                event.getName(),
                event.getDate(),
                event.getStartTime(),
                event.getLocation(),
                event.getDescription(),
                event.getTags(),
                event.getImage(),
                event.isPublished(),
                organizer == null ? null : OrganizerProfile.from(organizer));
    }

    public static List<EventProfile> fromList(List<Event> events) {
        return events.stream().map(EventProfile::from).toList();
    }
}