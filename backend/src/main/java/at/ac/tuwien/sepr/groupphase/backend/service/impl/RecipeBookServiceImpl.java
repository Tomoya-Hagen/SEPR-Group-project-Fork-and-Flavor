package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class RecipeBookServiceImpl implements RecipeBookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeBookRepository recipeRepository;

    public RecipeBookServiceImpl(RecipeBookRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public RecipeBook createRecipeBook(RecipeBookCreateDto recipeBookCreateDto) {
        LOGGER.trace("createRecipeBook({})", recipeBookCreateDto);
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.setId(recipeBookCreateDto.id());
        recipeBook.setName(recipeBookCreateDto.name());
        recipeBook.setDescription(recipeBookCreateDto.description());
        recipeBook.setOwnerId(recipeBookCreateDto.ownerId());
        recipeBook.setUserRecipeBooks(recipeBookCreateDto.userRecipeBooks());
        recipeBook.setRecipeRecipeBooks(recipeBookCreateDto.recipeRecipeBooks());

        return recipeRepository.save(recipeBook);
    }

}
