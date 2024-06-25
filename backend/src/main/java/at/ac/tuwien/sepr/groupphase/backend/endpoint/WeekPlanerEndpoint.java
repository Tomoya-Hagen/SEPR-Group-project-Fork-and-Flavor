package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryResultDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SimpleRecipeResultDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.service.CategoryService;
import at.ac.tuwien.sepr.groupphase.backend.service.WeekPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

/**
 * This is the WeekPlanerEndpoint class. It is a REST controller that handles HTTP requests related to weekPlaner.
 * It uses the WeekPlanerService to perform operations related to weekPlaner.
 */
@RestController
@RequestMapping(value = "/api/v1/weekplaner")
public class WeekPlanerEndpoint {


    WeekPlanService weekPlanService;

    public WeekPlanerEndpoint(WeekPlanService weekPlanService) {
        this.weekPlanService = weekPlanService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @Operation(summary = "Getting categories", security = @SecurityRequirement(name = "apiKey"))
    public WeekPlanDetailDto[] get(@PathVariable(name = "id") Long id,
                                   @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                   @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        LOGGER.info("POST /api/v1/weekplaner params: {}", id);
        return weekPlanService.getWeekplanDetail(id, from, to);
    }

}
