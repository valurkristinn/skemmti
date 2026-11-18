package is.hi.skemmti.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTests extends BaseControllerTests {

    private static final String NEW_USERNAME = "Nyr Notandi";
    private static final String VALID_PASSWORD = "password123";
    private static final String SHORT_PASSWORD = "short";
    private static final String SHORT_USERNAME = "ab";
    private static final String WRONG_PASSWORD = "wrongpassword";

    @Test
    void loginWithCorrectPassword() throws Exception {
        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(MEMBER, SEED_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyString())));
    }

    @Test
    void loginWithWrongPassword() throws Exception {
        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(MEMBER, WRONG_PASSWORD)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authHeaderIsAccepted() throws Exception {
        mvc.perform(get("/user")
                .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(MEMBER));
    }

    @Test
    void signupNewUser() throws Exception {
        mvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(NEW_USERNAME, VALID_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyString())));
    }

    @Test
    void signupTakenUsername() throws Exception {
        mvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(MEMBER, VALID_PASSWORD)))
                .andExpect(status().isConflict());
    }

    @Test
    void signupShortPassword() throws Exception {
        mvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(NEW_USERNAME, SHORT_PASSWORD)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signupOrgNewUser() throws Exception {
        mvc.perform(post("/signup/org")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(NEW_USERNAME, VALID_PASSWORD)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.username").value(NEW_USERNAME));
    }

    @Test
    void signupOrgShortUsername() throws Exception {
        mvc.perform(post("/signup/org")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(SHORT_USERNAME, VALID_PASSWORD)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signupUsernameTakenByPendingOrgRequest() throws Exception {
        mvc.perform(post("/signup/org")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(NEW_USERNAME, VALID_PASSWORD)))
                .andExpect(status().isAccepted());

        mvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(NEW_USERNAME, VALID_PASSWORD)))
                .andExpect(status().isConflict());
    }
}
