package is.hi.skemmti.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavouriteAndGoingControllerTests extends BaseControllerTests {

    private static final int EVENT_ID = 1;
    private static final int NOT_GOING_EVENT_ID = 3;
    private static final int UNKNOWN_EVENT_ID = 999;
    private static final String NEW_MEMBER = "Nyr.Medlimur";
    private static final String NEW_PASSWORD = "Password123";

    private HttpHeaders newMemberHeader() throws Exception {
        String token = mvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(NEW_MEMBER, NEW_PASSWORD)))
                .andReturn().getResponse().getContentAsString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    @Test
    void favouriteOrganizer_newMember_returns200() throws Exception {
        mvc.perform(post("/user/favourite/" + idOf(ORGANIZER))
                        .headers(newMemberHeader()))
                .andExpect(status().isOk());
    }

    @Test
    void favouriteOrganizer_alreadyFavourite_returns409() throws Exception {
        mvc.perform(post("/user/favourite/" + idOf(ORGANIZER))
                        .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isConflict());
    }

    @Test
    void favouriteOrganizer_notAnOrganizer_returns403() throws Exception {
        mvc.perform(post("/user/favourite/" + idOf(ADMIN))
                        .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void unfavouriteOrganizer_existingFavourite_returns200() throws Exception {
        mvc.perform(post("/user/unfavourite/" + idOf(ORGANIZER))
                        .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isOk());
    }

    @Test
    void unfavouriteOrganizer_notAFavourite_returns404() throws Exception {
        mvc.perform(post("/user/unfavourite/" + idOf(ORGANIZER))
                        .headers(newMemberHeader()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFavourites_member_returnsOne() throws Exception {
        mvc.perform(get("/user/favourite")
                        .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getFavourites_noToken_returns401() throws Exception {
        mvc.perform(get("/user/favourite"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getGoing_member_returnsTwo() throws Exception {
        mvc.perform(get("/user/going")
                        .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getGoing_noToken_returns401() throws Exception {
        mvc.perform(get("/user/going"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void markGoing_newMember_returns200() throws Exception {
        mvc.perform(post("/event/" + EVENT_ID + "/going")
                        .headers(newMemberHeader()))
                .andExpect(status().isOk());
    }

    @Test
    void markGoing_alreadyGoing_returns409() throws Exception {
        mvc.perform(post("/event/" + EVENT_ID + "/going")
                        .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isConflict());
    }

    @Test
    void markGoing_asOrganizer_returns403() throws Exception {
        mvc.perform(post("/event/" + EVENT_ID + "/going")
                        .headers(getAuthHeader(ORGANIZER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void cancelGoing_going_returns200() throws Exception {
        mvc.perform(post("/event/" + EVENT_ID + "/notgoing")
                        .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isOk());
    }

    @Test
    void cancelGoing_notGoing_returns404() throws Exception {
        mvc.perform(post("/event/" + NOT_GOING_EVENT_ID + "/notgoing")
                        .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAttendees_event_returnsOne() throws Exception {
        mvc.perform(get("/event/" + EVENT_ID + "/going")
                        .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAttendees_unknownEvent_returns404() throws Exception {
        mvc.perform(get("/event/" + UNKNOWN_EVENT_ID + "/going")
                        .headers(getAuthHeader(MEMBER)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAttendeeCount_event_returnsOne() throws Exception {
        mvc.perform(get("/event/" + EVENT_ID + "/going/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void getAttendeeCount_unknownEvent_returns404() throws Exception {
        mvc.perform(get("/event/" + UNKNOWN_EVENT_ID + "/going/count"))
                .andExpect(status().isNotFound());
    }
}
