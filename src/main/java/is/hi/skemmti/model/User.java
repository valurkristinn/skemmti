package is.hi.skemmti.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String image;
    private String description;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    private List<Event> attendingEvents;

    @ManyToMany
    private List<User> favoriteOrganizers;

    public User() {
        // TODO: útfæra
    }

    public Long getUserId() {
        // TODO: útfæra
        return null;
    }

    public void setUserId(Long userId) {
        // TODO: útfæra
    }

    public String getUsername() {
        // TODO: útfæra
        return null;
    }

    public void setUsername(String username) {
        // TODO: útfæra
    }

    public String getImage() {
        // TODO: útfæra
        return null;
    }

    public void setImage(String image) {
        // TODO: útfæra
    }

    public String getDescription() {
        // TODO: útfæra
        return null;
    }

    public void setDescription(String description) {
        // TODO: útfæra
    }

    public Role getRole() {
        // TODO: útfæra
        return null;
    }

    public void setRole(Role role) {
        // TODO: útfæra
    }

    public List<Event> getAttendingEvents() {
        // TODO: útfæra
        return null;
    }

    public void setAttendingEvents(List<Event> attendingEvents) {
        // TODO: útfæra
    }

    public List<User> getFavoriteOrganizers() {
        // TODO: útfæra
        return null;
    }

    public void setFavoriteOrganizers(List<User> favoriteOrganizers) {
        // TODO: útfæra
    }
}
