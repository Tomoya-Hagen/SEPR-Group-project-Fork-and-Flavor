package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@Service
public class RecipeServiceImpl implements RecipeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    @Override
    public Stream<RecipeListDto> searchRecipes(String name) {
        LOGGER.debug("Find recipe with name");
        var recipes = recipeRepository.search(name);

        return recipes.stream()
            .map(recipeMapper::recipeToRecipeListDto);
    }

}
