package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryResultDto;
import at.ac.tuwien.sepr.groupphase.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

/**
 * This is the CategoryEndpoint class. It is a REST controller that handles HTTP requests related to categories.
 * It uses the CategoryService to perform operations related to categories.
 */
@RestController
@RequestMapping(value = "/api/v1/category")
public class CategoryEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CategoryService categoryService;


    @Autowired
    public CategoryEndpoint(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Getting categories", security = @SecurityRequirement(name = "apiKey"))
    public Stream<CategoryResultDto> get(@RequestParam("name") String name, @RequestParam("limit") int limit) {
        LOGGER.info("POST /api/v1/category params: {} {}", name, limit);
        return categoryService.byname(name, limit);
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @Operation(summary = "Getting categories", security = @SecurityRequirement(name = "apiKey"))
    public Stream<CategoryResultDto> get() {
        LOGGER.info("POST /api/v1/category all");
        return categoryService.all();
    }
}
