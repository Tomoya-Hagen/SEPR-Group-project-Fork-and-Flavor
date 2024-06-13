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
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserManager;
import at.ac.tuwien.sepr.groupphase.backend.service.validators.RecipeBookValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    private final RecipeBookValidator recipeBookValidator;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserManager userManager;

    public RecipeBookServiceImpl(RecipeBookRepository recipeBookRepository,
                                 RecipeMapper recipeMapper,
                                 RecipeBookMapper recipeBookMapper,
                                 RecipeRepository recipeRepository,
                                 UserRepository userRepository,
                                 RecipeBookValidator recipeBookValidator,
                                 UserManager userManager) {
        this.recipeBookRepository = recipeBookRepository;
        this.recipeBookMapper = recipeBookMapper;
        this.recipeRepository = recipeRepository;
        this.recipeBookRecipeMapper = recipeMapper;
        this.userRepository = userRepository;
        this.recipeBookValidator = recipeBookValidator;
        this.userManager = userManager;
    }

    @Override
    public RecipeBookDetailDto getRecipeBookDetailDtoById(long id) throws NotFoundException {
        RecipeBook recipeBook = recipeBookRepository.findById(id).orElseThrow(NotFoundException::new);
        return recipeBookMapper.recipeBookToRecipeBookDetailDto(recipeBook);
    }

    @Override
    public Page<RecipeBookListDto> getRecipeBooksPageable(Pageable pageable) {
        Page<RecipeBook> recipeBooksPage = recipeBookRepository.findAll(pageable);
        return recipeBooksPage.map(recipeBookMapper::recipeBookToRecipeBookListDto);
    }

    @Override
    public Page<RecipeBookListDto> searchRecipeBooksByName(String name, Pageable pageable) {
        Page<RecipeBook> recipeBooksPage = recipeBookRepository.findByNameContainingIgnoreCaseOrderByName(name, pageable);
        return recipeBooksPage.map(recipeBookMapper::recipeBookToRecipeBookListDto);
    }

    @Override
    public RecipeBookDetailDto addRecipeToRecipeBook(long recipeBookId, long recipeId) throws NotFoundException, ForbiddenException, DuplicateObjectException {
        LOGGER.trace("addRecipeToRecipeBook({}, {})", recipeBookId, recipeId);
        RecipeBook recipeBook = recipeBookRepository.findById(recipeBookId).orElseThrow(() -> new NotFoundException("recipe book not found"));
        ApplicationUser currentUser = userManager.getCurrentUser();
        if (!(recipeBook.getOwnerId().equals(currentUser.getId()))
            && recipeBook.getEditors().stream().noneMatch(e -> e.getId() == currentUser.getId())
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
    public List<RecipeBookListDto> getRecipeBooksThatAnUserHasAccessTo() throws NotFoundException {
        LOGGER.trace("getRecipeBooksThatAUserHasAccessToByUserId()");
        ApplicationUser user = userManager.getCurrentUser();
        return recipeBookMapper.recipeBookListToRecipeBookListDto(recipeBookRepository
            .findRecipeBooksByOwnerOrSharedUser(user.getId()));
    }

    @Override
    public RecipeBookDetailDto createRecipeBook(@Valid RecipeBookCreateDto recipeBookCreateDto) throws ValidationException {
        LOGGER.trace("createRecipeBook({})", recipeBookCreateDto);
        recipeBookValidator.validateForCreateAndUpdate(recipeBookCreateDto);
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.setName(recipeBookCreateDto.name());
        recipeBook.setDescription(recipeBookCreateDto.description());

        ApplicationUser owner = userManager.getCurrentUser();
        recipeBook.setOwner(owner);
        List<Long> userIds = recipeBookCreateDto.users().stream().map(UserListDto::id).toList();
        List<ApplicationUser> users = userRepository.findAllById(userIds);

        recipeBook.setEditors(users);
        recipeBook.setRecipes(recipeBookRecipeMapper.listOfRecipeListDtoToRecipeList(recipeBookCreateDto.recipes()));
        recipeBookRepository.save(recipeBook);
        return recipeBookMapper.recipeBookToRecipeBookDetailDto(recipeBook);
    }

    @Override
    public void updateRecipeBook(Long id, RecipeBookCreateDto recipeBookCreateDto) throws ValidationException, NotFoundException {
        LOGGER.trace("updateRecipeBook({})", recipeBookCreateDto);
        recipeBookValidator.validateForCreateAndUpdate(recipeBookCreateDto);
        recipeBookRepository.findById(id).orElseThrow(NotFoundException::new);
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.setName(recipeBookCreateDto.name());
        recipeBook.setDescription(recipeBookCreateDto.description());

        ApplicationUser owner = userManager.getCurrentUser();
        recipeBook.setOwner(owner);
        List<Long> userIds = recipeBookCreateDto.users().stream().map(UserListDto::id).toList();
        List<ApplicationUser> users = userRepository.findAllById(userIds);

        recipeBook.setId(id);
        recipeBook.setEditors(users);
        recipeBook.setRecipes(recipeBookRecipeMapper.listOfRecipeListDtoToRecipeList(recipeBookCreateDto.recipes()));
        recipeBookRepository.save(recipeBook);
    }
}
