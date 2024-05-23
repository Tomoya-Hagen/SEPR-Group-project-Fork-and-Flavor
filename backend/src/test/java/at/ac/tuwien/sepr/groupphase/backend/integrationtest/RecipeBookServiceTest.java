package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeBookMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RecipeBookServiceTest {
    @Autowired
    RecipeBookRepository recipeBookRepository;
    @Autowired
    RecipeBookService recipeBookService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeBookMapper recipeBookMapper;
    @Test
    void serviceShouldThrowANotFoundExceptionIfARecipeIsAddedToARecipeBookThatDoesNotExist(){
        Assertions.assertThrows(NotFoundException.class, () -> recipeBookService.addRecipeToRecipeBook(16L,3L, "admin@email.com"));
    }

    @Test
    void serviceShouldThrowANotFoundExceptionIfARecipeBookDoesNotExist(){
        Assertions.assertThrows(NotFoundException.class, () -> recipeBookService.addRecipeToRecipeBook(20L,1L, "admin@email.com"));
    }

    @Test
    void serviceShouldThrowADuplicateObjectExceptionIfARecipeIsAddedToARecipeBookThatAlreadyContainsTheRecipe(){
        Assertions.assertThrows(DuplicateObjectException.class, () -> recipeBookService.addRecipeToRecipeBook(16L,1L, "admin@email.com"));
    }
    @Test
    void serviceShouldAddARecipeToARecipeBook(){
        RecipeBookDetailDto recipeBookDetailDto = recipeBookService.addRecipeToRecipeBook(16L,2L, "admin@email.com");
        Assertions.assertEquals(1,recipeBookDetailDto.recipes().stream().filter(r->r.id()==2L).count());
    }

    @Test
    void serviceShouldThrowAMethodForbiddenExceptionIfTheUserIsNotTheOwnerOrInTheUsersListOfAnRecipeBook(){
        Assertions.assertThrows(ForbiddenException.class, () -> recipeBookService.addRecipeToRecipeBook(16L,2L,"not allowed username"));
    }

    @Test
    void serviceShouldThrowANotFoundExceptionIfTheGivenUserIdDoesNotExistWhenTryingToRequestTheRecipeBooksThatAUserHasAccessTo(){
        Assertions.assertThrows(NotFoundException.class, () -> recipeBookService.getRecipeBooksThatAnUserHasAccessToByUserId(-1L, "admin@email.com"));
    }

    @Test
    void serviceShouldThrowAForbiddenExceptionIfTheGivenUserIdDoesNotHaveAccessRightsWhenTryingToRequestTheRecipeBooksThatAUserHasAccessTo(){
        Assertions.assertThrows(ForbiddenException.class, () -> recipeBookService.getRecipeBooksThatAnUserHasAccessToByUserId(1L, "test@email.com"));
    }

    @Test
    void serviceShouldReturnAllRecipeBooksThatAnUserHasWriteRightsFor(){
        RecipeBook recipeBook=recipeBookRepository.findById(15L).get();
        List<ApplicationUser> editors = recipeBook.getEditors();
        editors.add(userRepository.findById(1L).get());
        recipeBook.setEditors(editors);
        recipeBookRepository.save(recipeBook);
        List<RecipeBookListDto> recipeBookListDtos = recipeBookService.getRecipeBooksThatAnUserHasAccessToByUserId(1L,"admin@email.com");
        Assertions.assertAll(
            () -> Assertions.assertFalse(recipeBookListDtos.isEmpty()),
            () -> Assertions.assertEquals(7,recipeBookListDtos.size()),
            () -> Assertions.assertTrue(recipeBookListDtos.contains(
                recipeBookMapper.recipeBookListToRecipeBookListDto(List.of(recipeBook)).getFirst()))
        );
    }
}
