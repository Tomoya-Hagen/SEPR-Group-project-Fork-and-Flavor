package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.DetailedRecipeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeIngredientDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeStepDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeUpdateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.RecipeStepMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeIngredient;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeIngredientRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RecipeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private RecipeStepMapper recipeStepMapper;


    @Test
    void EditRecipeSuccessfullyChangingTheStepsIngredientsAndTheNumberOfServings() {
        List<RecipeStepDto> recipeStepDtoList = new ArrayList<>();
        recipeStepDtoList.add(new RecipeStepDto("Dawai dawai","Schneller",0,true));
        recipeStepDtoList.add(new RecipeStepDto("Tamam hamam","Mit Sorgfalt rühren",0,true));

        Recipe recipe = recipeRepository.getById(1L);
        ArrayList<RecipeStepDto> recipeSteps = new ArrayList<>();
        for (RecipeStep step : recipe.getRecipeSteps()) {
            recipeSteps.add(recipeStepMapper.recipeStepToRecipeStepDto(step));
        }

        recipeSteps.addAll(recipeStepDtoList);


        List<RecipeIngredient> newIngredients = new ArrayList<>();
        newIngredients.add(recipeIngredientRepository.getById(1L));

        List<RecipeIngredientDto> newDtoList = new ArrayList<>();

        for (RecipeIngredient recipeIngredient : newIngredients) {
            RecipeIngredient.Unit unit = recipeIngredient.getUnit();
            String unitString = "";
            switch(unit) {
                case g -> unitString = "g";
                case mg -> unitString = "mg";
                case L -> unitString = "L";
            }
            newDtoList.add(new RecipeIngredientDto(recipeIngredient.getId(), recipeIngredient.getAmount(), unitString));
        }

        List<RecipeCategoryDto> categorieDtos = new ArrayList<>();
        for (Category c : recipe.getCategories()) {
            categorieDtos.add(new RecipeCategoryDto(c.getId()));
        }

        categorieDtos.add(new RecipeCategoryDto(2L));

        RecipeUpdateDto updateDto = new RecipeUpdateDto(1L, recipe.getName(), recipe.getDescription(), (short) 3,
            categorieDtos, recipeSteps, newDtoList
            );

        DetailedRecipeDto d = recipeService.updateRecipe(updateDto);
        Recipe updatedRecipe = recipeRepository.getById(d.getId());

        assertEquals(updatedRecipe.getId(), recipe.getId());
        assertEquals(3, (int) updatedRecipe.getNumberOfServings());
        assertEquals(newIngredients.size(), updatedRecipe.getIngredients().size());
        assertEquals(1, updatedRecipe.getIngredients().size());
        assertEquals(updatedRecipe.getIngredients().getFirst().getIngredient().getId(), newDtoList.getFirst().getId());
        assertEquals(recipeSteps.size(), updatedRecipe.getRecipeSteps().size());
        List<RecipeStepDto> testSteps = new ArrayList<>();
        for (RecipeStep step : updatedRecipe.getRecipeSteps()) {
            testSteps.add(recipeStepMapper.recipeStepToRecipeStepDto(step));
        }
        assertTrue(testSteps.containsAll(recipeSteps));
        assertEquals(2, updatedRecipe.getCategories().size());
    }
}
