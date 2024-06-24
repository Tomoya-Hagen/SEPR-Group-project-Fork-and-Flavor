package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeBookMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserManager;
import at.ac.tuwien.sepr.groupphase.backend.service.validators.RecipeBookValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
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
    private final EmailService emailService;
    private final CategoryRepository categoryRepository;

    public RecipeBookServiceImpl(RecipeBookRepository recipeBookRepository,
                                 RecipeMapper recipeMapper,
                                 RecipeBookMapper recipeBookMapper,
                                 RecipeRepository recipeRepository,
                                 UserRepository userRepository,
                                 RecipeBookValidator recipeBookValidator,
                                 UserManager userManager,
                                 EmailService emailService, CategoryRepository categoryRepository) {
        this.recipeBookRepository = recipeBookRepository;
        this.recipeBookMapper = recipeBookMapper;
        this.recipeRepository = recipeRepository;
        this.recipeBookRecipeMapper = recipeMapper;
        this.userRepository = userRepository;
        this.recipeBookValidator = recipeBookValidator;
        this.userManager = userManager;
        this.emailService = emailService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public RecipeBookDetailDto getRecipeBookDetailDtoById(long id) throws NotFoundException {
        RecipeBook recipeBook = recipeBookRepository.findById(id).orElseThrow(NotFoundException::new);
        return recipeBookMapper.recipeBookToRecipeBookDetailDto(recipeBook, checkIfCanBeMadeWeekplan(recipeBook));
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
        return recipeBookMapper.recipeBookToRecipeBookDetailDto(recipeBook, checkIfCanBeMadeWeekplan(recipeBook));
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
        recipeBookValidator.validateForCreate(recipeBookCreateDto);
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.setName(recipeBookCreateDto.name());
        recipeBook.setDescription(recipeBookCreateDto.description());

        ApplicationUser owner = userManager.getCurrentUser();
        recipeBook.setOwner(owner);
        List<Long> userIds = recipeBookCreateDto.users().stream().map(UserListDto::id).toList();
        List<ApplicationUser> users = userRepository.findAllById(userIds);
        if (users.contains(owner)) {
            users.removeIf(user -> user.getId() == owner.getId());
            throw new ForbiddenException("The owner cannot be selected as one of the editors");
        }

        recipeBook.setEditors(users);
        recipeBook.setRecipes(recipeBookRecipeMapper.listOfRecipeListDtoToRecipeList(recipeBookCreateDto.recipes()));
        recipeBookRepository.save(recipeBook);
        return recipeBookMapper.recipeBookToRecipeBookDetailDto(recipeBook, checkIfCanBeMadeWeekplan(recipeBook));
    }

    @Override
    public void updateRecipeBook(Long id, RecipeBookUpdateDto recipeBookUpdateDto) throws ValidationException, NotFoundException {
        LOGGER.trace("updateRecipeBook({})", recipeBookUpdateDto);
        recipeBookValidator.validateForUpdate(recipeBookUpdateDto);
        var oldRecipeBook = recipeBookRepository.findById(id).orElseThrow(NotFoundException::new);
        List<ApplicationUser> newUsers = new ArrayList<>();
        if (!oldRecipeBook.getEditors().isEmpty()) {
            boolean alreadyExists = recipeBookUpdateDto.users().isEmpty();
            if (!alreadyExists) {
                for (var newEditors : recipeBookUpdateDto.users()) {
                    for (var editors : oldRecipeBook.getEditors()) {
                        if (editors.getId() == newEditors.id()) {
                            alreadyExists = true;
                            break;
                        }
                    }

                    if (!alreadyExists) {
                        newUsers.add(userRepository.findFirstById(newEditors.id()));
                    }
                }
            }
        } else {
            for (var editors : recipeBookUpdateDto.users()) {
                newUsers.add(userRepository.findFirstById(editors.id()));
            }
        }
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.setName(recipeBookUpdateDto.name());
        recipeBook.setDescription(recipeBookUpdateDto.description());

        ApplicationUser owner = userRepository.findFirstById(recipeBookUpdateDto.ownerId());
        recipeBook.setOwner(owner);
        List<Long> userIds = recipeBookUpdateDto.users().stream().map(UserListDto::id).toList();
        List<ApplicationUser> users = userRepository.findAllById(userIds);

        recipeBook.setId(id);
        recipeBook.setEditors(users);
        recipeBook.setRecipes(recipeBookRecipeMapper.listOfRecipeListDtoToRecipeList(recipeBookUpdateDto.recipes()));
        recipeBookRepository.save(recipeBook);

        for (var editors : newUsers) {
            LOGGER.trace("name {}", editors.getUsername());
            emailService.sendSimpleEmail(editors.getEmail(), "Zum neuen Rezeptbuch hinzugefügt: " + recipeBook.getName(), "Sie wurden zu einem neuen Rezeptbuch" + recipeBook.getName() + " hinzugefügt.");
        }
    }

    @Override
    public long getUserIdByRecipeBookId(Long id) throws NotFoundException {
        return recipeBookRepository.findById(id).orElseThrow(NotFoundException::new).getOwnerId();
    }

    /**
     * Checks if the recipebook could be made into a weekplan.
     * Needs to contain at least one breakfast and main dish.
     *
     * @param recipeBook The recipebook to check
     * @return true or false
     */
    private boolean checkIfCanBeMadeWeekplan(RecipeBook recipeBook) {
        boolean containsBreakfast = false;
        boolean containsMainDish = false;
        Category breakfast = categoryRepository.findByNameContainingIgnoreCase("Frühstück", PageRequest.of(0, 1)).getFirst();
        Category mainDish = categoryRepository.findByNameContainingIgnoreCase("Hauptspeise", PageRequest.of(0,1)).getFirst();
        for (Recipe recipe : recipeBook.getRecipes()) {
            if (recipe.getCategories().contains(breakfast)) {
                containsBreakfast = true;
            }
            if (recipe.getCategories().contains(mainDish)) {
                containsMainDish = true;
            }
        }
        return containsBreakfast && containsMainDish;
    }
}
