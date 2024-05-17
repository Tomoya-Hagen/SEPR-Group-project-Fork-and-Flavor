package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles({"test", "generateData"})
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecipeServiceTest {

    @Autowired
    RecipeService recipeService;

    RecipeListDto recipeListDto = new RecipeListDto(1,"Schnitzel","BLABLA",2L);

    @Test
    public void searchRecipeReturnsTrue(){
        var search = "Schnitzel";
        var recipe = recipeService.searchRecipes(search);
        assertNotNull(recipe);
    }


}
