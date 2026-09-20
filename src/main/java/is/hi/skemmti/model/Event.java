package com.example.eventapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String name;
    private LocalDate date;
    private LocalTime startTime;
    private String location;
    private String description;
    private String tag;
    private String image;

    @ManyToOne
    private User organizer;

    public Event() {
        // TODO: útfæra
    }

    public Long getEventId() {
        // TODO: útfæra
        return null;
    }

    public void setEventId(Long eventId) {
        // TODO: útfæra
    }

    public String getName() {
        // TODO: útfæra
        return null;
    }

    public void setName(String name) {
        // TODO: útfæra
    }

    public LocalDate getDate() {
        // TODO: útfæra
        return null;
    }

    public void setDate(LocalDate date) {
        // TODO: útfæra
    }

    public LocalTime getStartTime() {
        // TODO: útfæra
        return null;
    }

    public void setStartTime(LocalTime startTime) {
        // TODO: útfæra
    }

    public String getLocation() {
        // TODO: útfæra
        return null;
    }

    public void setLocation(String location) {
        // TODO: útfæra
    }

    public String getDescription() {
        // TODO: útfæra
        return null;
    }

    public void setDescription(String description) {
        // TODO: útfæra
    }

    public String getTag() {
        // TODO: útfæra
        return null;
    }

    public void setTag(String tag) {
        // TODO: útfæra
    }

    public String getImage() {
        // TODO: útfæra
        return null;
    }

    public void setImage(String image) {
        // TODO: útfæra
    }

    public User getOrganizer() {
        // TODO: útfæra
        return null;
    }

    public void setOrganizer(User organizer) {
        // TODO: útfæra
    }
}
