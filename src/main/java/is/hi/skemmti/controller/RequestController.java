package is.hi.skemmti.controller;

import is.hi.skemmti.model.OrganizerRequest;
import is.hi.skemmti.model.User;
import is.hi.skemmti.service.OrganizerRequestService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/requests/org")
public class RequestController {

    private final OrganizerRequestService organizerRequestService;

    public RequestController(OrganizerRequestService organizerRequestService){
        this.organizerRequestService = organizerRequestService;
    }

    @GetMapping
    public List<OrganizerRequest> getPendingRequests(){
        return organizerRequestService.getPendingRequests();
    }

    @PostMapping("/approve/{requestId}")
    public User approve(@PathVariable Long requestId){
        return organizerRequestService.approve(requestId);
    }

    @PostMapping("/reject/{requestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@PathVariable Long requestId){
        organizerRequestService.reject(requestId);
    }
    
}
