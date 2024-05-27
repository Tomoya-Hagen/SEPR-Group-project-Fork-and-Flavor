package at.ac.tuwien.sepr.groupphase.backend.unittests;

import static org.junit.jupiter.api.Assertions.*;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
public class RecipeBookServiceTest {

    @Autowired
    private RecipeBookService recipeBookService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private Validator validator;

    @Test
    public void createRecipeBookSuccessfully() {
        List<UserListDto> userRecipeBooks = new ArrayList<>();
        UserListDto userListDto = new UserListDto(3L, "a");
        userRecipeBooks.add(userListDto);
        List<Recipe> recipes = recipeRepository.getRecipeByIds(List.of(1L, 2L));
        List<RecipeListDto> r = recipeMapper.recipesToRecipeListDto(recipes);

        RecipeBookCreateDto createDto = new RecipeBookCreateDto( "Fast Food", "This recipe contains fast food dishes",
            userRecipeBooks, r);

        RecipeBookDetailDto recipeBook = recipeBookService.createRecipeBook(createDto, 1L);

        assertAll(
            () -> assertNotNull(recipeBook),
            () -> assertEquals(1L, recipeBook.recipes().getFirst().id()),
            () -> assertEquals(2L, recipeBook.recipes().get(1).id()),
            () -> assertEquals("Fast Food", recipeBook.name()),
            () -> assertEquals("This recipe contains fast food dishes", recipeBook.description()),
            () -> assertEquals(1L, recipeBook.ownerId()),
            () -> assertEquals(2, recipeBook.recipes().size()),
            () -> assertEquals(3L, recipeBook.users().getFirst().id())
        );
    }

    @Test
    public void recipeCreationFailsIfNameIsNull() {
        List<UserListDto> users = new ArrayList<>();
        UserListDto userListDto = new UserListDto(3L, "Admin");
        List<Recipe> recipes = recipeRepository.getRecipeByIds(List.of(2L, 3L));
        users.add(userListDto);
        RecipeBookCreateDto createDto = new RecipeBookCreateDto(null, null,
            users, recipeMapper.recipesToRecipeListDto(recipes));
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            validate(createDto);
            recipeBookService.createRecipeBook(createDto, 1L);
        });
        assertTrue(exception.getMessage().contains("Validation failed for name: must not be null"));
    }

    @Test
    public void recipeCreationFailsIfNameTooLong() {
        List<UserListDto> users = new ArrayList<>();
        UserListDto userListDto = new UserListDto(4L, "User");
        users.add(userListDto);
        List<Recipe> recipes = recipeRepository.getRecipeByIds(List.of(2L, 3L));
        List<RecipeListDto> recipeListDtos = recipeMapper.recipesToRecipeListDto(recipes);

        String longName = "a".repeat(101);
        RecipeBookCreateDto createDto = new RecipeBookCreateDto(longName, null,
            users, recipeListDtos);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            validate(createDto);
            recipeBookService.createRecipeBook(createDto, 1L);
        });
        assertTrue(exception.getMessage().contains("between 1 and 100"));
    }


    private void validate(@Valid RecipeBookCreateDto createDto) {
        validator.validate(createDto).forEach(violation -> {
            throw new ConstraintViolationException("Validation failed for " + violation.getPropertyPath() + ": " + violation.getMessage(), null);
        });
    }
}
