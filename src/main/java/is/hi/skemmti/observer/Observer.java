package is.hi.skemmti.observer;

import is.hi.skemmti.model.User;

public interface Observer {
    void handleEvent(User user);
}
