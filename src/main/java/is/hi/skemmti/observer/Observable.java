package is.hi.skemmti.observer;

import is.hi.skemmti.model.User;

public interface Observable {
    void addObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers(User event);
}
