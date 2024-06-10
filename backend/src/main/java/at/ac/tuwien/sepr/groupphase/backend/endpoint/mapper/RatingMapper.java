package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {UserMapper.class})
public interface RatingMapper {
    List<RatingListDto> mapListOfRatingToListOfRatingListDto(List<Rating> list);

    @Mapping(source = "rating.recipe.id", target = "recipeId")
    RatingListDto mapRatingToRatingListDto(Rating rating);

    Rating mapRatingCreateDtoToRating(RatingCreateDto ratingCreateDto);
}
