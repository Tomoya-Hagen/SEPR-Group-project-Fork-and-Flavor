package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeDescriptionStepRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeIngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRecipeStepRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class FullRecipeService implements RecipeService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeRecipeStepRepository recipeRecipeStepRepository;
    private final RecipeDescriptionStepRepository recipeDescriptionStepRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeMapper recipeMapper;

    public FullRecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper,
                             CategoryRepository categoryRepository, RecipeRecipeStepRepository recipeRecipeStepRepository,
                             RecipeDescriptionStepRepository recipeDescriptionStepRepository, RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.categoryRepository = categoryRepository;
        this.recipeRecipeStepRepository = recipeRecipeStepRepository;
        this.recipeDescriptionStepRepository = recipeDescriptionStepRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    @Override
    public DetailedRecipeDto createRecipe(RecipeCreateDto recipeDto) {
        LOGGER.debug("Publish new message {}", recipeDto);

        Recipe r = new Recipe();
        r.setName("Name");
        r.setDescription("Desc");
        r.setNumberOfServings((short)25);
        r.setOwnerId(1);

        Set<Category> categories = new HashSet<>();
        Optional<Category> optionalCategory = categoryRepository.findById(2l);
        optionalCategory.ifPresent(categories::add);
        r.setCategories(categories);

        recipeRepository.save(r);


//        Recipe recipe = recipeMapper.recipeCreateDtoToRecipe(recipeDto, recipeRepository.findMaxId() + 1);
//        var recipeIng = recipe.getRecipeIngredients();
//        var recipeStep = recipe.getRecipeSteps();
//
//        recipe.setRecipeIngredients(null);
//        recipe.setRecipeSteps(null);
//
//        recipeRepository.save(recipe);
//
//        recipeIngredientRepository.saveAll(recipeIng);
//
//
//        DetailedRecipeDto detailedRecipeDto = recipeMapper.recipeToDetailedRecipeDto(recipe);
//        return detailedRecipeDto;
        return null;
    }
}
