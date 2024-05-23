package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeBookMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Transactional
@Service
public class RecipeBookServiceImpl implements RecipeBookService {
    private final RecipeBookRepository recipeBookRepository;
    private final RecipeBookMapper recipeBookMapper;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public RecipeBookServiceImpl(RecipeBookRepository recipeBookRepository,
                                 RecipeBookMapper recipeMapper,
                                 RecipeRepository recipeRepository,
                                 UserRepository userRepository) {
        this.recipeBookRepository = recipeBookRepository;
        this.recipeBookMapper = recipeMapper;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RecipeBookDetailDto getRecipeBookDetailDtoById(long id) throws NotFoundException {
        RecipeBook recipeBook = recipeBookRepository.findById(id).orElseThrow(NotFoundException::new);
        return recipeBookMapper.recipeBookToRecipeBookDetailDto(recipeBook);
    }

    @Override
    public List<RecipeBookListDto> getRecipeBooks() {
        List<RecipeBook> allRecipeBooks = recipeBookRepository.findAll();
        return recipeBookMapper.recipeBookListToRecipeBookListDto(allRecipeBooks);
    }

    @Override
    public List<RecipeBookListDto> searchRecipeBooks(String name) throws NotFoundException {
        List<RecipeBook> searchedRecipeBooks = recipeBookRepository.search(name);
        return recipeBookMapper.recipeBookListToRecipeBookListDto(searchedRecipeBooks);
    }

    @Override
    public List<RecipeBookListDto> getRecipesFromPageInSteps(int pageNumber, int stepNumber) {
        int from = ((pageNumber - 1) * stepNumber) + 1;
        int to = pageNumber * stepNumber;
        List<RecipeBook> recipes = recipeBookRepository.getAllRecipesWithIdFromTo(from, to);
        return recipeBookMapper.recipeBookListToRecipeBookListDto(recipes);
    }

    @Override
    public RecipeBookDetailDto addRecipeToRecipeBook(long recipeBookId, long recipeId, String email) throws NotFoundException, ForbiddenException, DuplicateObjectException {
        LOGGER.trace("addRecipeToRecipeBook({}, {})", recipeBookId, recipeId);
        RecipeBook recipeBook = recipeBookRepository.findById(recipeBookId).orElseThrow(() -> new NotFoundException("recipe book not found"));
        if (!(userRepository.existsById(recipeBook.getOwnerId())
            && userRepository.findById(recipeBook.getOwnerId())
            .orElseThrow(() -> new NotFoundException("user with the given id not found"))
            .getEmail().equals(email))
            && recipeBook.getEditors().stream().noneMatch(e -> e.getEmail().equals(email))
        ) {
            throw new ForbiddenException("user has no writing access to this recipe book");
        }
        Recipe recipe = recipeRepository.getRecipeById(recipeId).orElseThrow(() -> new NotFoundException("recipe not found"));
        List<Recipe> recipes = recipeBook.getRecipes();
        if (recipes.contains(recipe)) {
            throw new DuplicateObjectException("Recipe is already in this recipe book");
        }
        recipes.add(recipe);
        recipeBook.setRecipes(recipes);
        recipeBookRepository.save(recipeBook);
        return recipeBookMapper.recipeBookToRecipeBookDetailDto(recipeBook);
    }

    @Override
    public List<RecipeBookListDto> getRecipeBooksThatAnUserHasAccessToByUserId(long userId, String email) throws NotFoundException, ForbiddenException {
        LOGGER.trace("getRecipeBooksThatAUserHasAccessToByUserId({})", userId);
        if (!userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("user with the given id not found"))
            .getEmail().equals(email)) {
            throw new ForbiddenException("This user is not allowed to access this method");
        }
        return recipeBookMapper.recipeBookListToRecipeBookListDto(recipeBookRepository
            .getRecipeBooksThatAnUserHasWriteAccessToByUserId(userId));
    }
}
