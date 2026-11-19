package is.hi.skemmti.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@OpenAPIDefinition(
        info = @Info(
                title = "Skemmti API",
                version = "sprint 2",
                description = """
                        **How to log in**
                        1. Open `POST /login`, click **Try it out**, enter `{"username": "admin", "password": "passw"}` and click **Execute**.
                        2. Copy the token from the response body.
                        3. Click **Authorize** (top right), paste the token (without the word Bearer) and click **Authorize**.

                        Test users, all with password `passw`: `admin` (admin), `oktoberfest` (organizer), `jon.jonsson` (member).
                        To switch user: Authorize → Logout, then paste the new user's token.

                        Usernames: 3–32 characters, only lowercase letters, numbers, `.`, `_` and `-` (e.g. `jon.jonsson`), capitals are converted to lowercase.
                        Passwords: 8–32 characters, at least one number and one capital letter (A–Z), no spaces (e.g. `Password123`).
                        """),
        security = @SecurityRequirement(name = "bearerAuth"))
public class OpenApiConfig {
}