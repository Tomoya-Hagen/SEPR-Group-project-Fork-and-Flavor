package at.ac.tuwien.sepr.groupphase.backend.basetest;

import java.util.ArrayList;
import java.util.List;

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

    String AUTH_BASE_URI = BASE_URI + "/authentication";

}
