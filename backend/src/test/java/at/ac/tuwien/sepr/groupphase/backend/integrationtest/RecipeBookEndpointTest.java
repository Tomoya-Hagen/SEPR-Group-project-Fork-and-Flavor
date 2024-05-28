package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


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

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void searchRecipeBooksReturnsRecipeBook() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/search")
                .param("name", "Familienrezepte")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", org.hamcrest.Matchers.is("Familienrezepte")));
    }

    @Test
    public void searchRecipeBooksReturnsEmptyListWhenNoMatch() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/search")
                .param("name", "Nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getNonExistingIdReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/999/details"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getListByPageAndStepReturnsRecipeBooks() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/")
                .param("page", "1")
                .param("step", "1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", org.hamcrest.Matchers.is("Italienische Küche")));
    }

    @Test
    public void getListByPageAndStepReturnsEmptyListWhenNoMatch() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/")
                .param("page", "-1")
                .param("step", "1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getListByPageAndStepReturnsBadRequestWhenPageIsNotNumber() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/")
                .param("page", "notANumber")
                .param("step", "1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getListByPageAndStepReturnsBadRequestWhenStepIsNotNumber() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook/")
                .param("page", "1")
                .param("step", "notANumber")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getRecipeBookListReturnsRecipeBooks() throws Exception {
        mockMvc.perform(get("/api/v1/recipebook")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(15)));
    }
}
