package is.hi.skemmti.service;

import is.hi.skemmti.model.OrganizerRequest;
import is.hi.skemmti.model.User;

import java.util.List;

public interface OrganizerRequestService {

    OrganizerRequest createRequest(String username, String password);

    List<OrganizerRequest> getPendingRequests();

    User approve(Long requestID);

    void reject(Long requestID);


}
