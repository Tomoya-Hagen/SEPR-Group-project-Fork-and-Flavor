package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.RecipeBookEndpoint;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles({"test", "generateData"})
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecipeBookEndpointTest {

    private static final Logger log = LoggerFactory.getLogger(RecipeBookEndpointTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RecipeBookService recipeBookService;

    @InjectMocks
    private RecipeBookEndpoint recipeBookEndpoint;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeBookEndpoint)
            .setControllerAdvice(new ExceptionHandlerExceptionResolver())
            .build();
    }

    /*@Test
    public void searchRecipeBooksReturnsRecipeBook() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/search")
                .param("name", "Familienrezepte")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Familienrezepte")));
    }*/

    @Test
    public void searchRecipeBooksReturnsEmptyListWhenNoMatch() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/search")
                .param("name", "Nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    /*@Test
    public void getNonExistingIdReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/999/details"))
            .andExpect(status().isNotFound());
    }*/
}
