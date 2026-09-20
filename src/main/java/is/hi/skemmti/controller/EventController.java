package com.example.eventapp.controller;

import com.example.eventapp.model.Event;
import com.example.eventapp.model.User;
import com.example.eventapp.service.EventService;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // TODO: bæta við @GetMapping/@PostMapping/@PutMapping/@DeleteMapping o.fl. þegar endapunktar eru ákveðnir

    public Event getEvent(Long eventId) {
        // TODO: útfæra
        return null;
    }

    public List<Event> getAllEvents() {
        // TODO: útfæra
        return null;
    }

    public List<Event> searchEventsByDate(LocalDate date) {
        // TODO: útfæra
        return null;
    }

    public List<Event> searchEventsByOrganizer(Long organizerId) {
        // TODO: útfæra
        return null;
    }

    public Event createEvent(Long organizerId, Event event) {
        // TODO: útfæra
        return null;
    }

    public Event editEvent(Long organizerId, Long eventId, Event event) {
        // TODO: útfæra
        return null;
    }

    public void deleteEvent(Long adminId, Long eventId) {
        // TODO: útfæra
    }

    public void deleteOwnEvent(Long organizerId, Long eventId) {
        // TODO: útfæra
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
}
