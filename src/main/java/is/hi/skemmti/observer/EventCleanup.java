package is.hi.skemmti.observer;

import java.util.List;

import org.springframework.stereotype.Component;

import is.hi.skemmti.model.Event;
import is.hi.skemmti.model.User;
import is.hi.skemmti.repository.EventRepository;
import is.hi.skemmti.service.EventService;

@Component
class EventCleanup implements Observer {

    private final EventService eventService;
    private final EventRepository eventRepository;

    public EventCleanup(EventService eventService, EventRepository eventRepository) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    @Override
    public void handleEvent(User user) {
        List<Event> events = eventRepository.findByOrganizer_UserId(user.getUserId());
        for (Event e : events) {
            eventService.deleteOwnEvent(user.getUserId(), e.getEventId());
        }

    }
}
