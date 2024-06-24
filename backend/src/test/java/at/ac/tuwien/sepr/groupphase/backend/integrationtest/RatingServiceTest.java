package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.RatingService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
class RatingServiceTest implements TestData {
    @Autowired
    private RatingService ratingService;
    @Test
    void GetAllRatingsForARecipeByRecipeId() {
        List<RatingListDto> ratings = ratingService.getRatingsByRecipeId(1);
        Assertions.assertEquals(6, ratings.size());
    }

    @Test
    void CreateNewRatingForRecipe() throws ValidationException {
        List<RatingListDto> ratings = ratingService.getRatingsByRecipeId(1);
        Assertions.assertEquals(6,ratings.size());
        Assertions.assertEquals(6,ratings.size());
        userAuthenticationByEmail("admin@email.com");
        RatingCreateDto ratingCreateDto = new RatingCreateDto(1,2,3,4,"test review");
        RatingListDto newRating = ratingService.createRating(ratingCreateDto);
        ratings = ratingService.getRatingsByRecipeId(1);
        Assertions.assertEquals(7,ratings.size());
        Assertions.assertAll(
            () -> Assertions.assertEquals(ratingCreateDto.cost(), newRating.cost()),
            () -> Assertions.assertEquals(ratingCreateDto.recipeId(), newRating.recipeId()),
            () -> Assertions.assertEquals(ratingCreateDto.review(), newRating.review()),
            () -> Assertions.assertEquals(ratingCreateDto.easeOfPrep(), newRating.easeOfPrep()),
            () -> Assertions.assertEquals(ratingCreateDto.taste(), newRating.taste()),
            () -> Assertions.assertEquals(1, newRating.user().id())
        );
    }

    @Test
    void CreateNewRatingReturnsNotFoundExceptionIfRecipeDoesNotExist(){
        userAuthenticationByEmail("admin@email.com");
        RatingCreateDto ratingCreateDto = new RatingCreateDto(-1,2,3,4,"test review");
        Assertions.assertThrows(NotFoundException.class, () -> ratingService.createRating(ratingCreateDto));
    }

    @Test
    void CreateNewRatingReturnsDuplicateObjectExceptionIfARatingFromANUserToARecipeAlreadyExists(){
        userAuthenticationByEmail("user@email.com");
        RatingCreateDto ratingCreateDto = new RatingCreateDto(2,2,3,4,"test review");
        Assertions.assertThrows(DuplicateObjectException.class, () -> ratingService.createRating(ratingCreateDto));
    }
}
