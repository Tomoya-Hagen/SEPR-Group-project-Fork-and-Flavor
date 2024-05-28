package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles({"test", "generateData"})
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecipeBookServiceTest {
    @Autowired
    private RecipeBookService recipeBookService;

    @Test
    public void searchRecipeBooksReturnsRecipeBooks() {
        RecipeBookListDto recipeBookListDto = new RecipeBookListDto(15,"Familienrezepte","Klassische und beliebte Rezepte für die ganze Familie.",3);
        List<RecipeBookListDto> recipeBookListDtoList = new java.util.ArrayList<>(List.of());
        recipeBookListDtoList.add(recipeBookListDto);

        assertEquals(recipeBookService.searchRecipeBooks("Familienrezepte"),recipeBookListDtoList);
        assertEquals(recipeBookService.searchRecipeBooks("Familienrezepte").size(),1);

    }

    @Test
    public void searchRecipeBooksReturnsEmptyListWhenNoMatch() {
        assertEquals(recipeBookService.searchRecipeBooks("Nonexistent"),new java.util.ArrayList<>(List.of()));
        assertEquals(recipeBookService.searchRecipeBooks("Nonexistent").size(),0);
    }

    @Test
    public void getNonExistingIdReturnsNotFound() {
        assertThrows(NotFoundException.class, () -> recipeBookService.getRecipeBookDetailDtoById(999L));
    }
}
