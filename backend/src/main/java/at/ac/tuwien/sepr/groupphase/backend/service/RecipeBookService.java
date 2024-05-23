package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;

/**
 * Interface for the recipe book service.
 *
 */
public interface RecipeBookService {

    /**
     * This method creates a new recipe book.
     *
     * @param recipeBook the recipe book to create.
     * @param ownerId Owner ID to be passed to the recipe book, this is the ID of the current user.
     * @return The created recipe book.
     */
    RecipeBook createRecipeBook(RecipeBookCreateDto recipeBook, Long ownerId);
}
