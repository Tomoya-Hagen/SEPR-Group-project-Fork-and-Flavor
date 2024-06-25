package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * This interface provides methods to map between Rating, RatingListDto, and RatingCreateDto objects.
 * It uses UserMapper to perform any necessary user-related mapping.
 */
@Mapper(uses = {UserMapper.class})
public interface RatingMapper {

    /**
     * Maps a list of ratings to a list of rating list dto.
     *
     * @param list that contains the items that should be mapped.
     * @return the list of the mapped objects.
     */
    List<RatingListDto> mapListOfRatingToListOfRatingListDto(List<Rating> list);

    /**
     * Maps a rating to a rating list dto.
     *
     * @param rating the rating that should be mapped.
     * @return the mapped rating list dto.
     */
    @Mapping(source = "rating.recipe.id", target = "recipeId")
    RatingListDto mapRatingToRatingListDto(Rating rating);

    /**
     * Maps a RatingCreateDto to a rating entity.
     *
     * @param ratingCreateDto the dto that should be converted.
     * @return the new rating that contains the values of the given dto.
     */
    Rating mapRatingCreateDtoToRating(RatingCreateDto ratingCreateDto);
}
