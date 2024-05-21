package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecipeBookService {
    RecipeBookDetailDto getRecipeBookDetailDtoById(long id) throws NotFoundException;

    List<RecipeBookListDto> getRecipeBooks();

    List<RecipeBookListDto> searchRecipeBooks(String name) throws NotFoundException;
}
