package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
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
    public RecipeBook createRecipeBook(@Valid RecipeBookCreateDto recipeBookCreateDto, Long ownerId) {
        LOGGER.trace("createRecipeBook({}, {})", recipeBookCreateDto, ownerId);
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.setName(recipeBookCreateDto.name());
        recipeBook.setDescription(recipeBookCreateDto.description());
        recipeBook.setOwnerId(ownerId);
        List<Long> userIds = recipeBookCreateDto.users().stream().map(UserListDto::id).toList();
        List<ApplicationUser> users = userRepository.findAllById(userIds);
        recipeBook.setUsers(users);
        recipeBook.setRecipes(recipeMapper.listOfRecipeListDtoToRecipeList(recipeBookCreateDto.recipes()));
        return recipeRepository.save(recipeBook);
    }

}
