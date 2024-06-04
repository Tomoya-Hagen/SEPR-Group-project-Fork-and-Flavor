package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeBookMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RecipeBookServiceTest implements TestData {

    @Autowired
    private RecipeBookService recipeBookService;
    @Autowired
    RecipeBookRepository recipeBookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeBookMapper recipeBookMapper;

    @Test
    void searchRecipeBooksReturnsRecipeBooks() {
        RecipeBookListDto recipeBookListDto = new RecipeBookListDto(1, "Italienische Küche", "Eine Sammlung klassischer italienischer Rezepte von Pasta bis Pizza.", 1);
        List<RecipeBookListDto> recipeBookListDtoList = new java.util.ArrayList<>(List.of());
        recipeBookListDtoList.add(recipeBookListDto);

        assertEquals(recipeBookService.searchRecipeBooks("Italienische Küche"), recipeBookListDtoList);
        assertEquals(1, recipeBookService.searchRecipeBooks("Italienische Küche").size());

    }

    @Test
    void searchRecipeBooksReturnsEmptyListWhenNoMatch() {
        assertEquals(new java.util.ArrayList<>(List.of()), recipeBookService.searchRecipeBooks("Nonexistent"));
        assertEquals(0, recipeBookService.searchRecipeBooks("Nonexistent").size());
    }

    @Test
    void getNonExistingIdReturnsNotFound() {
        assertThrows(NotFoundException.class, () -> recipeBookService.getRecipeBookDetailDtoById(999L));
    }

    @Test
    void getListByPageAndStepReturnsRecipeBooks() {
        RecipeBookListDto recipeBookListDto = new RecipeBookListDto(1L, "Italienische Küche", "Eine Sammlung klassischer italienischer Rezepte von Pasta bis Pizza.", 1L);
        assertEquals(Collections.singletonList(recipeBookListDto), recipeBookService.getRecipeBooksFromPageInSteps(1, 1));
    }

    @Test
    void getListByPageAndStepReturnsEmptyListWhenNoMatch() {
        assertEquals(Collections.emptyList(), recipeBookService.getRecipeBooksFromPageInSteps(-1, 1));
    }

    @Test
    void getRecipeBookReturnAllRecipeBooks() {
        assertEquals(9, recipeBookService.getRecipeBooks().size());
    }

    @Test
    void serviceShouldThrowANotFoundExceptionIfARecipeIsAddedToARecipeBookThatDoesNotExist() {
        Assertions.assertThrows(NotFoundException.class, () -> recipeBookService.addRecipeToRecipeBook(1600L, 3L));
    }

    @Test
    void serviceShouldThrowANotFoundExceptionIfARecipeBookDoesNotExist() {
        Assertions.assertThrows(NotFoundException.class, () -> recipeBookService.addRecipeToRecipeBook(20L, 1L));
    }

    @Test
    void serviceShouldThrowADuplicateObjectExceptionIfARecipeIsAddedToARecipeBookThatAlreadyContainsTheRecipe() {
        userAuthenticationByEmail("admin@email.com");
        Assertions.assertThrows(DuplicateObjectException.class, () -> recipeBookService.addRecipeToRecipeBook(1L, 7L));
    }

    @Test
    void serviceShouldAddARecipeToARecipeBook() {
        userAuthenticationByEmail("admin@email.com");
        RecipeBookDetailDto recipeBookDetailDto = recipeBookService.addRecipeToRecipeBook(1L, 2L);
        Assertions.assertEquals(1, recipeBookDetailDto.recipes().stream().filter(r -> r.id() == 2L).count());
    }

    @Test
    void serviceShouldThrowAMethodForbiddenExceptionIfTheUserIsNotTheOwnerOrInTheUsersListOfAnRecipeBook() {
        Assertions.assertThrows(ForbiddenException.class, () -> recipeBookService.addRecipeToRecipeBook(1L, 2L));
    }

    @Test
    void serviceShouldReturnAllRecipeBooksThatAnUserHasWriteRightsFor() {
        userAuthenticationByEmail("user@email.com");
        RecipeBook recipeBook = recipeBookRepository.findById(1L).get();
        List<ApplicationUser> editors = recipeBook.getEditors();
        editors.add(userRepository.findById(2L).get());
        recipeBook.setEditors(editors);
        recipeBookRepository.save(recipeBook);
        recipeBookRepository.flush();
        List<RecipeBookListDto> recipeBookListDtos = recipeBookService.getRecipeBooksThatAnUserHasAccessTo();
        Assertions.assertAll(
            () -> Assertions.assertFalse(recipeBookListDtos.isEmpty()),
            () -> Assertions.assertEquals(1, recipeBookListDtos.size()),
            () -> Assertions.assertTrue(recipeBookListDtos.contains(
                recipeBookMapper.recipeBookListToRecipeBookListDto(List.of(recipeBook)).getFirst()))
        );
    }
}
