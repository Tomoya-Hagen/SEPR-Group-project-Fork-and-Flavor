package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.WeekPlanDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.DuplicateObjectException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ForbiddenException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.WeekPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/v1/weekplaner")
public class WeekPlanerEndpoint {


    WeekPlanService weekPlanService;

    public WeekPlanerEndpoint(WeekPlanService weekPlanService) {
        this.weekPlanService = weekPlanService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @Operation(summary = "Getting categories", security = @SecurityRequirement(name = "apiKey"))
    public WeekPlanDetailDto[] get(@PathVariable(name = "id") Long id,
                                   @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                   @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        LOGGER.info("POST /api/v1/weekplaner params: {}", id);
        return weekPlanService.getWeekplanDetail(id, from, to);
    }

    @Secured("USER_ROLE")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "create a new week planner", security = @SecurityRequirement(name = "apiKey"))
    public WeekPlanDetailDto[] create(@RequestBody WeekPlanCreateDto weekPlanCreateDto) {
        LOGGER.info("POST /api/v1/weekplaner");
        try {
            return weekPlanService.create(weekPlanCreateDto);
        } catch (ValidationException | DuplicateObjectException e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ForbiddenException e) {
            HttpStatus status = HttpStatus.FORBIDDEN;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }
}
