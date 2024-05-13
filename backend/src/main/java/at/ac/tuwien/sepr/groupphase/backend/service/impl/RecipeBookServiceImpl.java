package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeBookMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
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
    private final RecipeBookMapper recipeBookMapper;
    private final RecipeMapper recipeMapper;

    public RecipeBookServiceImpl(RecipeBookRepository recipeRepository, RecipeBookMapper recipeBookMapper, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeBookMapper = recipeBookMapper;
        this.recipeMapper = recipeMapper;
    }

    @Override
    public RecipeBook createRecipeBook(RecipeBookCreateDto recipeBookCreateDto) {
        LOGGER.trace("createRecipeBook({})", recipeBookCreateDto);
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.setId(recipeBookCreateDto.id());
        recipeBook.setName(recipeBookCreateDto.name());
        recipeBook.setDescription(recipeBookCreateDto.description());
        recipeBook.setOwnerId(recipeBookCreateDto.ownerId());
        recipeBook.setUserRecipeBooks(recipeBookMapper.UserRecipeBookDtoListToUserRecipeBookList(recipeBookCreateDto.userRecipeBooks()));
        recipeBook.setRecipes(recipeMapper.ListOfRecipeListDtoToRecipeList(recipeBookCreateDto.recipes()));
        return recipeRepository.save(recipeBook);
    }

}
