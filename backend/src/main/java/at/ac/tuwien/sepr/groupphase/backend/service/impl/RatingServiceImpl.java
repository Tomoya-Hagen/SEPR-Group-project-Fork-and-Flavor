package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RatingListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RatingMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Rating;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RatingRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepr.groupphase.backend.service.RatingService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserManager;
import at.ac.tuwien.sepr.groupphase.backend.service.validators.RatingValidator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class RatingServiceImpl implements RatingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeRepository recipeRepository;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final RatingValidator ratingValidator;
    private final UserManager userManager;
    private final EmailService emailService;

    public RatingServiceImpl(RecipeRepository recipeRepository,
                             RatingRepository ratingRepository,
                             RatingMapper ratingMapper,
                             RatingValidator ratingValidator,
                             UserManager userManager, EmailService emailService) {
        this.recipeRepository = recipeRepository;
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.ratingValidator = ratingValidator;
        this.userManager = userManager;
        this.emailService = emailService;
    }

    @Override
    public List<RatingListDto> getRatingsByRecipeId(long recipeId) throws NotFoundException {
        LOGGER.trace("getRatingsByRecipeId({})", recipeId);
        Optional<Recipe> recipe = recipeRepository.getRecipeById(recipeId);
        if (recipe.isEmpty()) {
            throw new NotFoundException("The recipe with the given id was not found");
        }
        List<Rating> ratings = ratingRepository.getRatingsByRecipeId(recipeId).stream().toList();
        return ratingMapper.mapListOfRatingToListOfRatingListDto(ratings);
    }

    @Override
    public RatingListDto createRating(RatingCreateDto ratingCreateDto) throws ValidationException {
        LOGGER.trace("createRating({})", ratingCreateDto);
        ratingValidator.validateForCreate(ratingCreateDto);
        ApplicationUser user = userManager.getCurrentUser();
        Recipe recipe = recipeRepository.getRecipeById(ratingCreateDto.recipeId()).orElseThrow(NotFoundException::new);
        if (ratingRepository.getRatingsByRecipeIdAndUserId(recipe.getId(), user.getId()).isPresent()) {
            throw new DuplicateObjectException("A rating to this recipe already exists");
        }
        Rating rating = ratingMapper.mapRatingCreateDtoToRating(ratingCreateDto);
        rating.setRecipe(recipe);
        rating.setUser(user);
        ratingRepository.save(rating);

        emailService.sendSimpleEmail(recipe.getOwner().getEmail(), "Neue Bewertung!", "Neue Bewertung für das Rezept " + recipe.getName() + " erhalten.\n\n"
            + rating.toEmailString() + "\n");

        return ratingMapper.mapRatingToRatingListDto(rating);
    }
}
