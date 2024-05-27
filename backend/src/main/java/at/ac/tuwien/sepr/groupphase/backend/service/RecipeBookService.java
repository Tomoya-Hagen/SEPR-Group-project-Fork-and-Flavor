package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import org.springframework.stereotype.Service;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;

import java.util.List;

@Service
public interface RecipeBookService {
    RecipeBookDetailDto getRecipeBookDetailDtoById(long id) throws NotFoundException;

    List<RecipeBookListDto> getRecipeBooks();

    List<RecipeBookListDto> searchRecipeBooks(String name) throws NotFoundException;

    List<RecipeBookListDto> getRecipesFromPageInSteps(int page, int step);

    /**
     * This method represents spooning which simply adds a new recipe to a recipe book.
     *
     * @param recipeBookId represents the id of an recipe book.
     * @param recipeId     represents the id of a recipe that should be added to a recipe book.
     * @param email        represents the email address of the currently logged in user.
     * @return the recipe book with the newly added recipe.
     * @throws NotFoundException  if the recipe or the recipe book does not exist.
     * @throws ForbiddenException if the user is not allowed to add a recipe to the recipe book
     * @throws DuplicateObjectException if the recipe already exists in the recipe book
     */
    RecipeBookDetailDto addRecipeToRecipeBook(long recipeBookId, long recipeId, String email) throws NotFoundException, ForbiddenException, DuplicateObjectException;

    /**
     * This method gets all recipe books that a user has write access.
     *
     * @param email  represents the email address of the currently logged in user.
     * @return all recipes for that a user has write access.
     * @throws NotFoundException  if the recipe or the recipe book does not exist.
     */
    List<RecipeBookListDto> getRecipeBooksThatAnUserHasAccessToByUserId(String email) throws NotFoundException;

    /**
     * This method creates a new recipe book.
     *
     * @param recipeBook the recipe book to create.
     * @param ownerId Owner ID to be passed to the recipe book, this is the ID of the current user.
     * @return The created recipe book.
     */
    RecipeBookDetailDto createRecipeBook(RecipeBookCreateDto recipeBook, Long ownerId);
}
