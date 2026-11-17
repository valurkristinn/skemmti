package is.hi.skemmti.observer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import is.hi.skemmti.model.User;

@Component
class UserDeletionPublisher implements Observable {

    private List<Observer> observers = new ArrayList<>();

    public UserDeletionPublisher(List<Observer> observers) {
        for (Observer o : observers) {
            addObserver(o);
        }
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(User user) {
        for (Observer o : observers) {
            o.handleEvent(user);
        }
    }
}
