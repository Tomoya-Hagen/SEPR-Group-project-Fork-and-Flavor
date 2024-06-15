package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.*;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
public class RecipeBookRepositoryTest implements TestData {

    @Autowired
    private RecipeBookRepository recipeBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeMapper recipeMapper;

    @Test
    public void findRecipeBooksByOwnerOrSharedUserReturnsRecipeBooksWhenUserIsOwner() {
        List<RecipeBook> result = recipeBookRepository.findRecipeBooksByOwnerOrSharedUser(1L);
        assertEquals(9, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    @Disabled
    public void findRecipeBooksByOwnerOrSharedUserReturnsRecipeBooksWhenUserIsSharedUser() {
        List<RecipeBook> result = recipeBookRepository.findRecipeBooksByOwnerOrSharedUser(2L);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    public void findRecipeBooksByOwnerOrSharedUserReturnsEmptyListWhenUserHasNoRecipeBooks() {
        List<RecipeBook> result = recipeBookRepository.findRecipeBooksByOwnerOrSharedUser(2L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findRecipeBooksByOwnerOrSharedUserReturnsEmptyListWhenUserDoesNotExist() {
        List<RecipeBook> result = recipeBookRepository.findRecipeBooksByOwnerOrSharedUser(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void updateRecipeBookReturnsCorrectRecipeBook() {
        RecipeBookCreateDto recipeBookCreateDto = new RecipeBookCreateDto("New Recipe Book", "New Description",new ArrayList<>(),new ArrayList<>());
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.setName(recipeBookCreateDto.name());
        recipeBook.setDescription(recipeBookCreateDto.description());

        ApplicationUser owner = new ApplicationUser();
        owner.setId(1L);
        recipeBook.setOwner(owner);
        List<Long> userIds = recipeBookCreateDto.users().stream().map(UserListDto::id).toList();
        List<ApplicationUser> users = userRepository.findAllById(userIds);

        recipeBook.setId(1L);
        recipeBook.setEditors(users);
        recipeBook.setRecipes(recipeMapper.listOfRecipeListDtoToRecipeList(recipeBookCreateDto.recipes()));
        recipeBookRepository.save(recipeBook);

        RecipeBook recipeBook2 = recipeBookRepository.findById(1L).orElseThrow(NotFoundException::new);
        assertEquals(recipeBook2.getName(),recipeBook.getName());
        assertEquals(recipeBook2.getId(),recipeBook.getId());
        assertEquals(recipeBook2.getOwnerId(),recipeBook.getOwnerId());
        assertEquals(recipeBook2.getRecipes().size(),recipeBook.getRecipes().size());
        assertEquals(recipeBook2.getEditors().size(),recipeBook.getEditors().size());
    }
}
