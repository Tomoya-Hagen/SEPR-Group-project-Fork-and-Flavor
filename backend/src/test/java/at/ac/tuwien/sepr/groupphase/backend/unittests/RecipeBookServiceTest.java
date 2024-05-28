package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
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
    public void searchReturnsRecipeBooksWhenNameMatches() {
        assertEquals(recipeBookRepository.search("Familienrezepte").getFirst().getName(), "Familienrezepte");
    }

    @Test
    public void searchReturnsEmptyListWhenNoNameMatches() {
        assertEquals(recipeBookRepository.search("Nonexistent"), Collections.emptyList());
    }

    @Test
    public void searchReturnsRecipeBooksRegardlessOfCase() {
        assertEquals(recipeBookRepository.search("Familien").getFirst().getName(), "Familienrezepte");
    }

    @Test
    public void searchReturnsEmptyListWhenNameIsNull() {
        assertEquals(recipeBookRepository.search(null).size(), 15);
    }

    @Test
    public void getAllRecipesWithIdFromToReturnsRecipesInIdRange() {
        List<RecipeBook> result = recipeBookRepository.getAllRecipesWithIdFromTo(1, 2);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    public void getAllRecipesWithIdFromToReturnsEmptyListWhenNoRecipesInIdRange() {
        List<RecipeBook> result = recipeBookRepository.getAllRecipesWithIdFromTo(100, 200);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllRecipesWithIdFromToReturnsRecipesInIdRangeWhenRangeIsSingleId() {

        List<RecipeBook> result = recipeBookRepository.getAllRecipesWithIdFromTo(1, 1);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
    }

    @Test
    public void createRecipeBookSuccessfully() {
        List<UserListDto> userRecipeBooks = new ArrayList<>();
        UserListDto userListDto = new UserListDto(3L, "a");
        userRecipeBooks.add(userListDto);
        List<Recipe> recipes = recipeRepository.getRecipeByIds(List.of(1L, 2L));
        List<RecipeListDto> r = recipeMapper.recipesToRecipeListDto(recipes);

        RecipeBookCreateDto createDto = new RecipeBookCreateDto("Fast Food", "This recipe contains fast food dishes",
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
