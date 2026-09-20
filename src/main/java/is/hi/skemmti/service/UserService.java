package com.example.eventapp.service;

import com.example.eventapp.model.Event;
import com.example.eventapp.model.User;

import java.util.List;

public interface UserService {

    User login(String username, String password);

    User signup(String username, String password);

    User getUser(Long userId);

    void favoriteOrganizer(Long memberId, Long organizerId);

    void unfavoriteOrganizer(Long memberId, Long organizerId);

    List<User> getFavoriteOrganizers(Long memberId);

    List<Event> getAttendingEvents(Long memberId);
}
