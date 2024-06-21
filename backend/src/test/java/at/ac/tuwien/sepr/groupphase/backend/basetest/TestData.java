package at.ac.tuwien.sepr.groupphase.backend.basetest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public interface TestData {

    Long ID = 1L;
    String BASE_URI = "/api/v1";
    String MESSAGE_BASE_URI = BASE_URI + "/messages";
    String RECIPE_BOOK_BASE_URI = BASE_URI + "/recipebook";

    String ADMIN_USER = "admin@email.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String RECIPE_BASE_URI = BASE_URI + "/recipes";
    String RATING_BASE_URI = BASE_URI + "/ratings";

    String WEEKPLAN_BASE_URI = BASE_URI + "/weekplaner";


    String AUTH_BASE_URI = BASE_URI + "/authentication";

default void userAuthenticationByEmail(String email){
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);

    when(securityContext.getAuthentication()).thenReturn(authentication);

    SecurityContextHolder.setContext(securityContext);

    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(email);
    }
}
