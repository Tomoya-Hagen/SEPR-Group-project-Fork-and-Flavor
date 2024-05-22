package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeBookMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeBookRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeBookService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class RecipeBookServiceImpl implements RecipeBookService {
    private final RecipeBookRepository recipeBookRepository;
    private final RecipeBookMapper recipeBookMapper;

    public RecipeBookServiceImpl(RecipeBookRepository recipeRepository,
                                 RecipeBookMapper recipeMapper) {
        this.recipeBookRepository = recipeRepository;
        this.recipeBookMapper = recipeMapper;
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
}
