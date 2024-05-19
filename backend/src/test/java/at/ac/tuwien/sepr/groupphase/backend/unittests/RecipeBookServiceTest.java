package at.ac.tuwien.sepr.groupphase.backend.unittests;

import static org.junit.jupiter.api.Assertions.*;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RecipeBookServiceTest {

    @Autowired
    private RecipeBookService recipeBookService;

    @Autowired
    private RecipeMapper recipeMapper;

    @Test
    public void createRecipeBookSuccessfully() {
        List<UserListDto> userRecipeBooks = new ArrayList<>();
        UserListDto userListDto = new UserListDto(-1L, "a");
        userRecipeBooks.add(userListDto);
        List<RecipeListDto> recipes = new ArrayList<>();
        RecipeListDto r1 = new RecipeListDto(1L, "Pizza", "Go for it", 3);
        RecipeListDto r2 = new RecipeListDto(2L, "Curry", "Hi, I'm from India", 4);
        recipes.add(r1);
        recipes.add(r2);

        RecipeBookCreateDto createDto = new RecipeBookCreateDto(-1L, "Fast Food", "This recipe contains fast food dishes",
            -3L, userRecipeBooks, recipes);

        RecipeBook recipeBook = recipeBookService.createRecipeBook(createDto);

        assertAll(
            () -> assertNotNull(recipeBook),
            () -> assertEquals(1L, recipeBook.getRecipes().getFirst().getId())

        );
    }
}
