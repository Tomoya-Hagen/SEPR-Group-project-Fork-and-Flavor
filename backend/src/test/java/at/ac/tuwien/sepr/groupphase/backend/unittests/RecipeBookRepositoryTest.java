package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.*;
import at.ac.tuwien.sepr.groupphase.backend.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
}
