package is.hi.skemmti.controller;

import is.hi.skemmti.model.Role;
import is.hi.skemmti.service.OrganizerRequestService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RequestControllerTests extends BaseControllerTests {

    private static final Long UNKNOWN_REQUEST_ID = 999L;
    private static final String NEW_ORG_USERNAME = "Nytt Felag";
    private static final String NEW_ORG_PASSWORD = "password123";

    @Autowired
    private OrganizerRequestService organizerRequestService;

    private Long createRequest() {
        return organizerRequestService.createRequest(NEW_ORG_USERNAME, NEW_ORG_PASSWORD).getRequestId();
    }

    @Test
    void getRequestsAsAdmin() throws Exception {
        mvc.perform(get("/requests/org")
                .headers(getAuthHeader(ADMIN)))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestsAsMember() throws Exception {
        mvc.perform(get("/requests/org")
                .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void approveRequestAsAdmin() throws Exception {
        mvc.perform(post("/requests/org/approve/" + createRequest())
                .headers(getAuthHeader(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(Role.ORGANIZER.name()));
    }

    @Test
    void approveUnknownRequest() throws Exception {
        mvc.perform(post("/requests/org/approve/" + UNKNOWN_REQUEST_ID)
                .headers(getAuthHeader(ADMIN)))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectRequestAsAdmin() throws Exception {
        mvc.perform(post("/requests/org/reject/" + createRequest())
                .headers(getAuthHeader(ADMIN)))
                .andExpect(status().isNoContent());
    }

    @Test
    void rejectRequestAsMember() throws Exception {
        mvc.perform(post("/requests/org/reject/" + createRequest())
                .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isForbidden());
    }
}
