package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is the RecipeBookService interface. It is a service in the Spring framework that handles operations related to recipe books.
 * It defines methods for getting recipe book details by ID, getting a list of all recipe books, searching for recipe books by name, and getting a list of recipe books by page and step.
 */
@Service
public interface RecipeBookService {
    /**
     * This method is responsible for getting the details of a recipe book by its ID.
     *
     * @param id The ID of the recipe book.
     * @return A RecipeBookDetailDto object that contains the details of the recipe book.
     * @throws NotFoundException If no recipe book is found with the provided ID.
     */
    RecipeBookDetailDto getRecipeBookDetailDtoById(long id) throws NotFoundException;

    /**
     * This method is responsible for getting a list of all recipe books.
     *
     * @return A list of RecipeBookListDto objects that contain the details of all recipe books.
     */
    Page<RecipeBookListDto> getRecipeBooksPageable(Pageable pageable);

    /**
     * This method is responsible for searching for recipe books by name.
     *
     * @param name The name of the recipe book.
     * @return A list of RecipeBookListDto objects that contain the details of recipe books that match the search criteria.
     */
    Page<RecipeBookListDto> searchRecipeBooksByName(String name, Pageable pageable);

    /**
     * This method represents spooning which simply adds a new recipe to a recipe book.
     *
     * @param recipeBookId represents the id of an recipe book.
     * @param recipeId     represents the id of a recipe that should be added to a recipe book.
     * @return the recipe book with the newly added recipe.
     * @throws NotFoundException  if the recipe or the recipe book does not exist.
     * @throws ForbiddenException if the user is not allowed to add a recipe to the recipe book
     * @throws DuplicateObjectException if the recipe already exists in the recipe book
     */
    RecipeBookDetailDto addRecipeToRecipeBook(long recipeBookId, long recipeId) throws NotFoundException, ForbiddenException, DuplicateObjectException;

    /**
     * This method gets all recipe books that a user has write access.
     *
     * @return all recipes for that a user has write access.
     * @throws NotFoundException  if the recipe or the recipe book does not exist.
     */
    List<RecipeBookListDto> getRecipeBooksThatAnUserHasAccessTo() throws NotFoundException;

    /**
     * This method creates a new recipe book.
     *
     * @param recipeBook the recipe book to create.
     * @return The created recipe book.
     */
    RecipeBookDetailDto createRecipeBook(RecipeBookCreateDto recipeBook) throws ValidationException;

    /**
     * Updates an existing recipe book with the provided details.
     *
     * @param id The ID of the recipe book to be updated.
     * @param recipeBook The new details for the recipe book.
     * @throws ValidationException If the provided details are not valid.
     * @throws NotFoundException If no recipe book is found with the provided ID.
     */
    void updateRecipeBook(Long id, RecipeBookUpdateDto recipeBook) throws ValidationException, NotFoundException;

    /**
     * Retrieves the ID of the user associated with a given recipe book.
     *
     * @param id The ID of the recipe book.
     * @return The ID of the user associated with the recipe book.
     * @throws NotFoundException If no recipe book is found with the provided ID.
     */
    long getUserIdByRecipeBookId(Long id) throws NotFoundException;
}
