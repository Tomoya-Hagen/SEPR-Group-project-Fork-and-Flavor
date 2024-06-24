package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.CustomUserDetailService;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.RatingServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecommendationEngineTest implements TestData {
  @Autowired
  CustomUserDetailService customUserDetailService;

  @Autowired
  RecipeService recipeService;

  @Autowired
  RatingServiceImpl ratingService;
  @Autowired
  private RecipeRepository recipeRepository;
  @Autowired
  private UserRepository userRepository;

  @Test
  public void simple2wayrecommendationEngineTest() throws Exception {
    UserRegisterDto user1 = new UserRegisterDto("one@test.com","password","onetest");
    customUserDetailService.register(user1);


    createSimpleRatingOne(user1.email());

    UserRegisterDto user2 = new UserRegisterDto("two@test.com","password","twotest");
    customUserDetailService.register(user2);
    createSimpleRatingTwo(user2.email());

    UserRegisterDto user3 = new UserRegisterDto("three@test.com","password","threetest");
    customUserDetailService.register(user3);
    createSimpleRatingThree(user3.email());


    List<RecipeListDto> res = recipeService.getRecipesByRecommendation();
    assertEquals(6, res.size());

    ApplicationUser applicationUser1 = userRepository.findFirstUserByEmail(user1.email());
    ApplicationUser applicationUser2 = userRepository.findFirstUserByEmail(user2.email());
    ApplicationUser applicationUser3 = userRepository.findFirstUserByEmail(user3.email());
    List<Recipe> recone = recipeRepository.findAllRecipesByGoodInteraction(applicationUser1);
    List<Recipe> rectwo = recipeRepository.findAllRecipesByGoodInteraction(applicationUser2);
    List<Recipe> recthree = recipeRepository.findAllRecipesByGoodInteraction(applicationUser3);


    HashSet<Recipe> set1 = new HashSet<>(recone);
    HashSet<Recipe> set2 = new HashSet<>(rectwo);

    List<Recipe> uniqueToBoth = new ArrayList<>(set1);
    uniqueToBoth.removeAll(set2);

    List<Recipe> uniqueToList2 = new ArrayList<>(set2);
    uniqueToList2.removeAll(set1);

    uniqueToBoth.addAll(uniqueToList2);

    assertEquals(uniqueToBoth.size(),res.size());

    res = res.stream()
      .sorted(Comparator.comparingLong(RecipeListDto::id))
      .toList();
    uniqueToBoth = uniqueToBoth.stream()
      .sorted(Comparator.comparingLong(Recipe::getId))
      .toList();

    for(int i = 0; i < 6; i++){
      assertEquals(uniqueToBoth.get(i).getId(), res.get(i).id()) ;
    }

  }

  private void createSimpleRatingOne(String email) throws ValidationException {
    userAuthenticationByEmail(email);
    RatingCreateDto[] ratingarr = new RatingCreateDto[10];
    ratingarr[0] = new RatingCreateDto(10,4,5,4,"bla");
    ratingarr[1] = new RatingCreateDto(15,4,5,4,"bla");
    ratingarr[2] = new RatingCreateDto(17,4,5,4,"bla");
    ratingarr[3] = new RatingCreateDto(18,4,5,4,"bla");
    ratingarr[4] = new RatingCreateDto(19,4,5,4,"bla");
    ratingarr[5] = new RatingCreateDto(12,4,5,4,"bla");
    ratingarr[6] = new RatingCreateDto(21,4,5,4,"bla");

    ratingarr[7] = new RatingCreateDto(23,4,5,4,"bla");
    ratingarr[8] = new RatingCreateDto(24,4,5,4,"bla");
    ratingarr[9] = new RatingCreateDto(27,4,5,4,"bla");

    for(var rating: ratingarr){
      ratingService.createRating(rating);
    }
  }

  private void createSimpleRatingTwo(String email) throws ValidationException {
    userAuthenticationByEmail(email);
    RatingCreateDto[] ratingarr = new RatingCreateDto[10];
    ratingarr[0] = new RatingCreateDto(10,4,5,4,"bla");
    ratingarr[1] = new RatingCreateDto(15,4,5,4,"bla");
    ratingarr[2] = new RatingCreateDto(17,4,5,4,"bla");
    ratingarr[3] = new RatingCreateDto(18,4,5,4,"bla");
    ratingarr[4] = new RatingCreateDto(19,4,5,4,"bla");
    ratingarr[5] = new RatingCreateDto(12,4,5,4,"bla");
    ratingarr[6] = new RatingCreateDto(21,4,5,4,"bla");

    ratingarr[7] = new RatingCreateDto(33,4,5,4,"bla");
    ratingarr[8] = new RatingCreateDto(34,4,5,4,"bla");
    ratingarr[9] = new RatingCreateDto(35,4,5,4,"bla");

    for(var rating: ratingarr){
      ratingService.createRating(rating);
    }
  }
  private void createSimpleRatingThree(String email) throws ValidationException {
    userAuthenticationByEmail(email);
    RatingCreateDto[] ratingarr = new RatingCreateDto[10];
    ratingarr[0] = new RatingCreateDto(10,4,5,4,"bla");
    ratingarr[1] = new RatingCreateDto(15,4,5,4,"bla");
    ratingarr[2] = new RatingCreateDto(17,4,5,4,"bla");
    ratingarr[3] = new RatingCreateDto(18,4,5,4,"bla");
    ratingarr[4] = new RatingCreateDto(19,4,5,4,"bla");
    ratingarr[5] = new RatingCreateDto(12,4,5,4,"bla");
    ratingarr[6] = new RatingCreateDto(21,4,5,4,"bla");

    ratingarr[7] = new RatingCreateDto(41,4,5,4,"bla");
    ratingarr[8] = new RatingCreateDto(50,4,5,4,"bla");
    ratingarr[9] = new RatingCreateDto(76,4,5,4,"bla");

    for(var rating: ratingarr){
      ratingService.createRating(rating);
    }
  }
}
