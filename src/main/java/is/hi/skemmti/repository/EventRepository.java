package com.example.eventapp.repository;

import com.example.eventapp.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findById(Long eventId);

    List<Event> findAll();

    List<Event> findByDate(LocalDate date);

    List<Event> findByOrganizer_UserId(Long organizerId);

    Event save(Event event);

    void delete(Event event);
}
