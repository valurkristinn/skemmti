package com.example.eventapp.controller;

import com.example.eventapp.model.Event;
import com.example.eventapp.model.User;
import com.example.eventapp.service.UserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // TODO: bæta við @PostMapping/@GetMapping o.fl. þegar endapunktar eru ákveðnir

    public User login(String username, String password) {
        // TODO: útfæra
        return null;
    }

    public User signup(String username, String password) {
        // TODO: útfæra
        return null;
    }

    public User getUser(Long userId) {
        // TODO: útfæra
        return null;
    }

    public void favoriteOrganizer(Long memberId, Long organizerId) {
        // TODO: útfæra
    }

    public void unfavoriteOrganizer(Long memberId, Long organizerId) {
        // TODO: útfæra
    }

    public List<User> getFavoriteOrganizers(Long memberId) {
        // TODO: útfæra
        return null;
    }

    public List<Event> getAttendingEvents(Long memberId) {
        // TODO: útfæra
        return null;
    }
}
