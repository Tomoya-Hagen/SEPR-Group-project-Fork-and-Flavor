package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.util.List;

public interface RatingService {
    List<RatingListDto> getRatingsByRecipeId(long recipeId);

    RatingListDto createRating(RatingCreateDto ratingCreateDto) throws ValidationException;
}
