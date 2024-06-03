package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeBookMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
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
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Transactional
@Service
public class RecipeBookServiceImpl implements RecipeBookService {
    private final RecipeBookRepository recipeBookRepository;
    private final RecipeMapper recipeBookRecipeMapper;
    private final RecipeBookMapper recipeBookMapper;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public RecipeBookServiceImpl(RecipeBookRepository recipeBookRepository,
                                 RecipeMapper recipeMapper,
                                 RecipeBookMapper recipeBookMapper,
                                 RecipeRepository recipeRepository,
                                 UserRepository userRepository) {
        this.recipeBookRepository = recipeBookRepository;
        this.recipeBookMapper = recipeBookMapper;
        this.recipeRepository = recipeRepository;
        this.recipeBookRecipeMapper = recipeMapper;
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
    public List<RecipeBookListDto> searchRecipeBooks(String name) {
        List<RecipeBook> searchedRecipeBooks = recipeBookRepository.search(name);
        return recipeBookMapper.recipeBookListToRecipeBookListDto(searchedRecipeBooks);
    }

    @Override
    public List<RecipeBookListDto> getRecipeBooksFromPageInSteps(int pageNumber, int stepNumber) {
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
    public List<RecipeBookListDto> getRecipeBooksThatAnUserHasAccessToByUserId(String email) throws NotFoundException {
        LOGGER.trace("getRecipeBooksThatAUserHasAccessToByUserId()");
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException("user was not found");
        }
        ApplicationUser user = userRepository.findFirstUserByEmail(email);
        return recipeBookMapper.recipeBookListToRecipeBookListDto(recipeBookRepository
            .findRecipeBooksByOwnerOrSharedUser(user.getId()));
    }

    @Override
    public RecipeBookDetailDto createRecipeBook(@Valid RecipeBookCreateDto recipeBookCreateDto, Long ownerId) {
        LOGGER.trace("createRecipeBook({}, {})", recipeBookCreateDto, ownerId);
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.setName(recipeBookCreateDto.name());
        recipeBook.setDescription(recipeBookCreateDto.description());
        ApplicationUser owner = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("owner not found"));
        recipeBook.setOwner(owner);
        List<Long> userIds = recipeBookCreateDto.users().stream().map(UserListDto::id).toList();
        List<ApplicationUser> users = userRepository.findAllById(userIds);
        recipeBook.setEditors(users);
        recipeBook.setRecipes(recipeBookRecipeMapper.listOfRecipeListDtoToRecipeList(recipeBookCreateDto.recipes()));
        recipeBookRepository.save(recipeBook);
        return recipeBookMapper.recipeBookToRecipeBookDetailDto(recipeBook);
    }
}
