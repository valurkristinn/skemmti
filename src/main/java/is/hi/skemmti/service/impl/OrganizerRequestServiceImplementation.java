package is.hi.skemmti.service.impl;

import is.hi.skemmti.exception.ApiException.Conflict;
import is.hi.skemmti.exception.ApiException.NotFound;
import is.hi.skemmti.model.OrganizerRequest;
import is.hi.skemmti.model.Role;
import is.hi.skemmti.model.User;
import is.hi.skemmti.repository.OrganizerRequestRepository;
import is.hi.skemmti.repository.UserRepository;
import is.hi.skemmti.service.OrganizerRequestService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class OrganizerRequestServiceImplementation implements OrganizerRequestService {

    private final OrganizerRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OrganizerRequestServiceImplementation(OrganizerRequestRepository requestRepository,
            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OrganizerRequest createRequest(String username, String password){
        if (userRepository.findByUsername(username).isPresent() || requestRepository.existsByUsername(username)){
            throw new Conflict("Username is already taken");
        }

        OrganizerRequest request = new OrganizerRequest();
        request.setUsername(username);
        request.setPassword(passwordEncoder.encode(password));
        request.setCreatedAt(Instant.now());

        return requestRepository.save(request);
    }

    @Override
    public List<OrganizerRequest> getPendingRequests(){
        return requestRepository.findAll();
    }

    @Override
    public void reject(Long requestId){
        OrganizerRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new NotFound("Request not found"));
        
        requestRepository.delete(request);
    }

    @Override
    @Transactional
    public User approve(Long requestId){
        OrganizerRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new NotFound("Request not found"));

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new Conflict("Username is already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); //already hased in createRequest
        user.setRole(Role.ORGANIZER);

        User saved = userRepository.save(user);
        requestRepository.delete(request);
        return saved;
    }
}
