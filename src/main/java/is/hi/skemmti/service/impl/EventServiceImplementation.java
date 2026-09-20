package com.example.eventapp.service.impl;

import com.example.eventapp.model.Event;
import com.example.eventapp.model.User;
import com.example.eventapp.repository.EventRepository;
import com.example.eventapp.repository.UserRepository;
import com.example.eventapp.service.EventService;
import org.springframework.stereotype.Service;

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
        // TODO: útfæra
        return null;
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
        // TODO: útfæra - athuga að organizerId hafi Role.ORGANIZER áður en viðburður er búinn til
        return null;
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
    public void deleteOwnEvent(Long organizerId, Long eventId) {
        // TODO: útfæra - athuga að viðburðurinn tilheyri þessum skipuleggjanda
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
