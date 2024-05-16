package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientResultDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepNotParsableException;
import at.ac.tuwien.sepr.groupphase.backend.exception.RecipeStepSelfReferenceException;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeDescriptionStepRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeIngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRecipeStepRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeStepRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class FullRecipeService implements RecipeService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeStepRepository recipeStepRepository;
    private final UserRepository userRepository;

    public FullRecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper,
                             CategoryRepository categoryRepository, RecipeIngredientRepository recipeIngredientRepository,
                             RecipeStepRepository recipeStepRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.categoryRepository = categoryRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DetailedRecipeDto createRecipe(RecipeCreateDto recipeDto) throws RecipeStepNotParsableException, RecipeStepSelfReferenceException {
        LOGGER.debug("Publish new message {}", recipeDto);

        Recipe recipe = recipeMapper.recipeCreateDtoToRecipe(recipeDto, recipeRepository.findMaxId() + 1);

        Set<Category> categories = new HashSet<>();
        for (RecipeCategoryDto category : recipeDto.getCategories()) {
            Category c = new Category();
            c.setId(category.getId());
            categories.add(c);
        }

        recipe.setCategories(categories);

        var recipeIng = recipe.getRecipeIngredients();
        var recipeStep = recipe.getRecipeSteps();

        recipe.setRecipeIngredients(null);

        recipe.setRecipeSteps(null);

        var auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long ownerid = 0;
        if(auth instanceof String) {
            ownerid = userRepository.findFirstUserByEmail((String) auth).getId();
        }
        recipe.setOwnerId(ownerid);
        recipeRepository.save(recipe);

        recipeIngredientRepository.saveAll(recipeIng);

        recipeStepRepository.saveAll(recipeStep);

        return recipeMapper.recipeToDetailedRecipeDto(recipe);
    }

    @Override
    public Stream<SimpleRecipeResultDto> byname(String name, int limit) {
        var x = recipeRepository.findByNameContainingWithLimit(name, PageRequest.of(0,limit)).stream().map(recipeMapper::recipeToRecipeResultDto);
        return x;
    }
}
