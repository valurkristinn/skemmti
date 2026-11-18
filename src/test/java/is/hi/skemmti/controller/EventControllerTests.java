package is.hi.skemmti.controller;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class EventControllerTests extends BaseControllerTests {

    private static final int PUBLISHED_EVENT_ID = 1;
    private static final int UNPUBLISHED_EVENT_ID = 3;
    private static final String VALID_EVENT = """
            {"name":"Jolatonleikar","date":"2026-12-10","startTime":"20:00:00",
             "location":"Harpa","description":"Christmas concert"}
            """;

    private static final String EDITED_EVENT = """
            {"name":"Edited name","date":"2026-12-10","startTime":"21:00:00",
             "location":"Harpa","description":"Edited"}
            """;

    private static final String EVENT_WITHOUT_NAME = """
            {"date":"2026-12-10","startTime":"20:00:00",
             "location":"Harpa","description":"No name"}
            """;

    @Test
    void getAllEventsWithoutLogin() throws Exception {
        mvc.perform(get("/event"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getAllEventsWithNegativeOffset() throws Exception {
        mvc.perform(get("/event").param("offset", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPublishedEvent() throws Exception {
        mvc.perform(get("/event/" + PUBLISHED_EVENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizer.name").value(ORGANIZER));
    }

    @Test
    void getUnpublishedEvent() throws Exception {
        mvc.perform(get("/event/" + UNPUBLISHED_EVENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchEventsByTerm() throws Exception {
        mvc.perform(get("/event/search").param("searchTerm", "okt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void searchEventsWithTooLargeLimit() throws Exception {
        mvc.perform(get("/event/search").param("limit", "1000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrganizerEvents() throws Exception {
        mvc.perform(get("/org/" + idOf(ORGANIZER) + "/event"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getOrganizerEventsWithInvalidTime() throws Exception {
        mvc.perform(get("/org/" + idOf(ORGANIZER) + "/event").param("time", "tomorrow"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrganizerEventsForMember() throws Exception {
        mvc.perform(get("/org/" + idOf(MEMBER) + "/event"))
                .andExpect(status().isNotFound());
    }

@Test
void createEventAsOrganizer() throws Exception {
    mvc.perform(post("/event")
                    .headers(getAuthHeader(ORGANIZER))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_EVENT))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.organizer.name").value(ORGANIZER));
}

@Test
void createEventAsMember() throws Exception {
    mvc.perform(post("/event")
                    .headers(getAuthHeader(MEMBER))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_EVENT))
            .andExpect(status().isForbidden());
}

@Test
void createEventWithoutName() throws Exception {
    mvc.perform(post("/event")
                    .headers(getAuthHeader(ORGANIZER))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(EVENT_WITHOUT_NAME))
            .andExpect(status().isBadRequest());
}

@Test
void createEventWithoutLogin() throws Exception {
    mvc.perform(post("/event")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_EVENT))
            .andExpect(status().isUnauthorized());
}

@Test
void editOwnEvent() throws Exception {
    mvc.perform(put("/event/" + PUBLISHED_EVENT_ID)
                    .headers(getAuthHeader(ORGANIZER))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(EDITED_EVENT))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Edited name"));
}

@Test
void editEventAsMember() throws Exception {
    mvc.perform(put("/event/" + PUBLISHED_EVENT_ID)
                    .headers(getAuthHeader(MEMBER))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(EDITED_EVENT))
            .andExpect(status().isForbidden());
}

@Test
void adminEditsEvent() throws Exception {
    mvc.perform(put("/admin/event/" + PUBLISHED_EVENT_ID)
                    .headers(getAuthHeader(ADMIN))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(EDITED_EVENT))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Edited name"));
}

@Test
void adminEditAsMember() throws Exception {
    mvc.perform(put("/admin/event/" + PUBLISHED_EVENT_ID)
                    .headers(getAuthHeader(MEMBER))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(EDITED_EVENT))
            .andExpect(status().isForbidden());
}

@Test
void deleteOwnEvent() throws Exception {
    mvc.perform(delete("/event/" + PUBLISHED_EVENT_ID)
                    .headers(getAuthHeader(ORGANIZER)))
            .andExpect(status().isNoContent());
}

@Test
void deleteEventAsMember() throws Exception {
    mvc.perform(delete("/event/" + PUBLISHED_EVENT_ID)
                    .headers(getAuthHeader(MEMBER)))
            .andExpect(status().isForbidden());
}
}