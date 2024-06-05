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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Pageable pageable = PageRequest.of(0, 10);
        Page<RecipeBookListDto> result = recipeBookService.searchRecipeBooksByName("Italienische Küche", pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("Italienische Küche", result.getContent().getFirst().name());
    }

    @Test
    void searchRecipeBooksReturnsEmptyListWhenNoMatch() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RecipeBookListDto> result = recipeBookService.searchRecipeBooksByName("Nonexistent", pageable);
        assertTrue(result.isEmpty());
    }

    @Test
    void getNonExistingIdReturnsNotFound() {
        assertThrows(NotFoundException.class, () -> recipeBookService.getRecipeBookDetailDtoById(999L));
    }

    @Test
    void getListByPageAndStepReturnsRecipeBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RecipeBookListDto> result = recipeBookService.getRecipeBooksPageable(pageable);
        assertFalse(result.isEmpty());
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
            () -> assertFalse(recipeBookListDtos.isEmpty()),
            () -> Assertions.assertEquals(1, recipeBookListDtos.size()),
            () -> assertTrue(recipeBookListDtos.contains(
                recipeBookMapper.recipeBookListToRecipeBookListDto(List.of(recipeBook)).getFirst()))
        );
    }
}
