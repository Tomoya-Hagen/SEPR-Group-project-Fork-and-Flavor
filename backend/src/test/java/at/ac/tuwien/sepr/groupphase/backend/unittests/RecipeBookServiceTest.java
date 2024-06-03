package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RecipeBookServiceTest {
    @Autowired
    private RecipeBookService recipeBookService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private Validator validator;

    @Autowired
    private RecipeBookRepository recipeBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @BeforeAll
    static void setUp() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }

    @Test
    void searchReturnsRecipeBooksWhenNameMatches() {
        assertEquals("Indische Spezialitäten", recipeBookRepository.search("indische").getFirst().getName());
    }

    @Test
    void searchReturnsEmptyListWhenNoNameMatches() {
        assertEquals(Collections.emptyList(), recipeBookRepository.search("Nonexistent"));
    }

    @Test
    void searchReturnsRecipeBooksRegardlessOfCase() {
        assertEquals("Indische Spezialitäten", recipeBookRepository.search("inDISche").getFirst().getName());
    }

    @Test
    void searchReturnsEmptyListWhenNameIsNull() {
        assertEquals(9, recipeBookRepository.search(null).size());
    }

    @Test
    void getAllRecipesWithIdFromToReturnsRecipesInIdRange() {
        List<RecipeBook> result = recipeBookRepository.findByIdBetweenOrderById(1L, 2L);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getAllRecipesWithIdFromToReturnsEmptyListWhenNoRecipesInIdRange() {
        List<RecipeBook> result = recipeBookRepository.findByIdBetweenOrderById(100L, 200L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllRecipeBooksWithIdFromToReturnsRecipesInIdRangeWhenRangeIsSingleId() {

        List<RecipeBook> result = recipeBookRepository.findByIdBetweenOrderById(1L, 1L);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
    }

    @Test
    void createRecipeBookSuccessfully() throws ValidationException {
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
    void recipeBookCreationFailsIfNameIsNull() {
        List<UserListDto> users = new ArrayList<>();
        ApplicationUser user = userRepository.findFirstById(1L);
        List<Recipe> recipes = recipeRepository.getRecipeByIds(List.of(2L, 3L));
        users.add(userMapper.userToUserListDto(user));
        RecipeBookCreateDto createDto = new RecipeBookCreateDto(null, null,
            users, recipeMapper.recipesToRecipeListDto(recipes));
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            validate(createDto);
            recipeBookService.createRecipeBook(createDto, 1L);
        });
        assertTrue(exception.getMessage().contains("Validation failed for name: must not be null"));
    }

    @Test
    void recipeBookCreationFailsIfNameTooLong() {
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
