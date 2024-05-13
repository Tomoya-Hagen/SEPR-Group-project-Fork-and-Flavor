package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientResultDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface IngredientMapper {
    IngredientResultDto ingredientToIngredientResultDto(Ingredient ingredient);
}
