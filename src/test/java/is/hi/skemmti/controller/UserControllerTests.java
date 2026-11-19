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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

class UserControllerTests extends BaseControllerTests {

    private static final String NEW_USERNAME = "Nyr Notandi";
    private static final String VALID_PASSWORD = "password123";
    private static final String SHORT_PASSWORD = "short";
    private static final String SHORT_USERNAME = "ab";
    private static final String WRONG_PASSWORD = "wrongpassword";
    private static final Long UNKNOWN_ID = 999_999L;

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

    // GET /user

    @Test
    void getOwnAccount_loggedIn_returns200() throws Exception {
        mvc.perform(get("/user")
                .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(MEMBER));
    }

    @Test
    void getOwnAccount_noToken_returns401() throws Exception {
        mvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    // GET /user/{userId}

    @Test
    void getUser_existingUser_returnsPublicFieldsOnly() throws Exception {
        mvc.perform(get("/user/" + idOf(ORGANIZER))
                .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(ORGANIZER))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void getUser_unknownId_returns404() throws Exception {
        mvc.perform(get("/user/" + UNKNOWN_ID)
                .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isNotFound());
    }

    // GET /org/{orgId} (public)

    @Test
    void getOrganizer_organizer_returns200() throws Exception {
        mvc.perform(get("/org/" + idOf(ORGANIZER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(ORGANIZER));
    }

    @Test
    void getOrganizer_memberId_returns404() throws Exception {
        mvc.perform(get("/org/" + idOf(MEMBER)))
                .andExpect(status().isNotFound());
    }

    // PATCH /user

    @Test
    void editOwnAccount_validChange_returns200() throws Exception {
        mvc.perform(patch("/user")
                .headers(getAuthHeader(MEMBER))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Ný lýsing\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Ný lýsing"));
    }

    @Test
    void editOwnAccount_tooShortUsername_returns400() throws Exception {
        mvc.perform(patch("/user")
                .headers(getAuthHeader(MEMBER))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"ab\"}"))
                .andExpect(status().isBadRequest());
    }

    // PATCH /user/{userId}

    @Test
    void editUser_asAdmin_returns200() throws Exception {
        mvc.perform(patch("/user/" + idOf(MEMBER))
                .headers(getAuthHeader(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Breytt af admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Breytt af admin"));
    }

    @Test
    void editUser_asMemberOnSomeoneElse_returns403() throws Exception {
        mvc.perform(patch("/user/" + idOf(ORGANIZER))
                .headers(getAuthHeader(MEMBER))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"hakk\"}"))
                .andExpect(status().isForbidden());
    }

    // DELETE /user

    @Test
    void deleteOwnAccount_loggedIn_returns204() throws Exception {
        mvc.perform(delete("/user")
                .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteOwnAccount_noToken_returns401() throws Exception {
        mvc.perform(delete("/user"))
                .andExpect(status().isUnauthorized());
    }

    // DELETE /user/{userId}

    @Test
    void deleteUser_asAdmin_returns204() throws Exception {
        mvc.perform(delete("/user/" + idOf(MEMBER))
                .headers(getAuthHeader(ADMIN)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_asMemberOnSomeoneElse_returns403() throws Exception {
        mvc.perform(delete("/user/" + idOf(ORGANIZER))
                .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isForbidden());
    }
}
