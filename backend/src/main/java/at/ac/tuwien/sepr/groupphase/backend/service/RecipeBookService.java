package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
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
     * @return A list of RecipeBookListDto objects that represent all recipe books.
     */
    List<RecipeBookListDto> getRecipeBooks();

    /**
     * This method is responsible for searching for recipe books by name.
     *
     * @param name The name of the recipe book.
     * @return A list of RecipeBookListDto objects that represent the recipe books that match the search criteria.
     * @throws NotFoundException If no recipe books are found with the provided name.
     */
    List<RecipeBookListDto> searchRecipeBooks(String name) throws NotFoundException;

    /**
     * This method is responsible for getting a list of recipe books by page and step.
     *
     * @param page The page number.
     * @param step The step size.
     * @return A list of RecipeBookListDto objects that represent the recipe books on the specified page and step.
     */
    List<RecipeBookListDto> getRecipeBooksFromPageInSteps(int page, int step);
}
