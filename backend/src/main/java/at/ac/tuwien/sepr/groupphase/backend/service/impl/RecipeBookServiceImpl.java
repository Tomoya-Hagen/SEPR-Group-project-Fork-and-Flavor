package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeBookServiceImpl implements RecipeBookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeBookRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final UserRepository userRepository;

    public RecipeBookServiceImpl(RecipeBookRepository recipeRepository, RecipeMapper recipeMapper, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.userRepository = userRepository;
    }

    @Override
    public RecipeBook createRecipeBook(RecipeBookCreateDto recipeBookCreateDto) {
        LOGGER.trace("createRecipeBook({})", recipeBookCreateDto);
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.setName(recipeBookCreateDto.name());
        recipeBook.setDescription(recipeBookCreateDto.description());
        recipeBook.setOwnerId(recipeBookCreateDto.ownerId());
        List<UserListDto> users = recipeBookCreateDto.users();
        List<ApplicationUser> result = new ArrayList<>();
        for (UserListDto user : users) {
            result.add(userRepository.getById(user.id()));
        }
        recipeBook.setUsers(result);
        recipeBook.setRecipes(recipeMapper.listOfRecipeListDtoToRecipeList(recipeBookCreateDto.recipes()));
        return recipeRepository.save(recipeBook);
    }

}
