package is.hi.skemmti.controller;

import is.hi.skemmti.ContextBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

abstract class BaseControllerTests extends ContextBase {

    protected static final String SEED_PASSWORD = "passw";

    @Autowired
    protected MockMvc mvc;

    private String tokenFor(String username) throws Exception {
        return mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(username, SEED_PASSWORD)))
                .andReturn().getResponse().getContentAsString();
    }

    protected HttpHeaders getAuthHeader(String username) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenFor(username));
        return headers;
    }

    protected static String credentials(String username, String password) {
        return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
    }
}
