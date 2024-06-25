package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.FullRatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.util.List;

/**
 * This is the RatingService interface. It is a service in the Spring framework that handles operations related to ratings.
 * It defines methods for getting rating details by ID, getting a list of all rating and getting a list of ratings.
 */
public interface RatingService {

    /**
     * return all ratings based on the given recipe id.
     *
     * @param recipeId recipe Id for that should be filtered.
     * @return a list of ratings that have the given id.
     */
    List<RatingListDto> getRatingsByRecipeId(long recipeId);

    /**
     * create a new rating.
     *
     * @param ratingCreateDto contains the data given by the user to rate a recipe.
     * @return the new created rating
     * @throws ValidationException if the data given by the user was invalid.
     */
    RatingListDto createRating(RatingCreateDto ratingCreateDto) throws ValidationException;

    /**
     * Retrieves a list of RatingListDto objects associated with a specific user.
     *
     * @param id The ID of the user.
     * @return List of RatingListDto objects.
     * @throws NotFoundException If no user is found with the provided ID.
     */
    List<FullRatingListDto> getRatingsByUserId(long id) throws NotFoundException;
}
