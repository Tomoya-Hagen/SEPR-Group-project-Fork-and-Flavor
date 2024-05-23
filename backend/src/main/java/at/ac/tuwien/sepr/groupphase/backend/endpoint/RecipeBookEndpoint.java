package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(path = RecipeBookEndpoint.BASE_PATH)
public class RecipeBookEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/api/v1/recipebook";
    private final RecipeBookService recipeBookService;
    private final UserService userService;

    @Autowired
    public RecipeBookEndpoint(RecipeBookService service, UserService userService) {
        this.recipeBookService = service;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<RecipeBook> createRecipe(@RequestBody RecipeBookCreateDto recipeBook) {
        LOGGER.trace("Creating recipe book: {}", recipeBook);
        LOGGER.debug("Body of request: {}", recipeBook);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ApplicationUser user = userService.findApplicationUserByEmail(email);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(recipeBookService.createRecipeBook(recipeBook, user.getId()));
        } catch (Exception e) {
            LOGGER.warn("Error creating recipe book: {}", recipeBook, e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
