package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface RecipeService {
    RecipeDetailDto getRecipeDetailDtoById(long id) throws NotFoundException;
}
