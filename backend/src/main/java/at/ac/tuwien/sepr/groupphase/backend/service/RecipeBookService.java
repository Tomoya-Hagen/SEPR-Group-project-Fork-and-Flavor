package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;


public interface RecipeBookService {

    RecipeBook createRecipeBook(RecipeBookCreateDto recipeBook, Long ownerId);
}
