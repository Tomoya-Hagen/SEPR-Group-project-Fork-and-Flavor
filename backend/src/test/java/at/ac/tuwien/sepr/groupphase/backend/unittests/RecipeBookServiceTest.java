package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "generateData"})
@Transactional
public class RecipeBookServiceTest {
    @Autowired
    private RecipeBookRepository recipeBookRepository;

    @Test
    public void searchReturnsRecipeBooksWhenNameMatches() {
        assertEquals(recipeBookRepository.search("Familienrezepte").getFirst().getName(), "Familienrezepte");
    }

    @Test
    public void searchReturnsEmptyListWhenNoNameMatches() {
        assertEquals(recipeBookRepository.search("Nonexistent"),Collections.emptyList());
    }

    @Test
    public void searchReturnsRecipeBooksRegardlessOfCase() {
        assertEquals(recipeBookRepository.search("Familien").getFirst().getName(),"Familienrezepte");
    }

    @Test
    public void searchReturnsEmptyListWhenNameIsNull() {
        assertEquals(recipeBookRepository.search(null).size(),15);
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
}
