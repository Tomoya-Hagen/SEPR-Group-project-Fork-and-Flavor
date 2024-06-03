package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * This is the RecipeBookMapper interface. It is responsible for mapping between RecipeBook and RecipeBookListDto objects.
 * It uses MapStruct, a code generator that simplifies the implementation of mappings between Java bean types.
 */
@Mapper(uses = {RecipeStepMapper.class, CategoryMapper.class, AllergenMapper.class,
    IngredientMapper.class, NutritionMapper.class, UserMapper.class})
public interface RecipeBookMapper {

    /**
     * This method is responsible for mapping a list of RecipeBook objects to a list of RecipeBookListDto objects.
     * It uses the @Mapping annotation to specify that the 'id' field of the RecipeBook object should be mapped to the 'id' field of the RecipeBookListDto object.
     *
     * @param recipeBook A list of RecipeBook objects that need to be mapped to RecipeBookListDto objects.
     * @return A list of RecipeBookListDto objects that have been mapped from the provided list of RecipeBook objects.
     */
    List<RecipeBookListDto> recipeBookListToRecipeBookListDto(List<RecipeBook> recipeBook);

    /**
     * This method is responsible for mapping a  RecipeBook object to a RecipeBookListDto object.
     * It uses the @Mapping annotation to specify that the 'id' field of the RecipeBook object should be mapped to the 'id' field of the RecipeBookListDto object.
     *
     * @param recipeBook A RecipeBook object that need to be mapped to RecipeBookListDto object.
     * @return A RecipeBookListDto object that have been mapped from the provided RecipeBook object.
     */
    @Mapping(source = "recipeBook.id", target = "id")
    RecipeBookListDto recipeBookToRecipeBookListDto(RecipeBook recipeBook);

    /**
     * This method is responsible for mapping a RecipeBook object to a RecipeBookDetailDto object.
     * It does not require any specific field mappings as all fields in the RecipeBook object have corresponding fields in the RecipeBookDetailDto object.
     *
     * @param recipeBook A RecipeBook object that needs to be mapped to a RecipeBookDetailDto object.
     * @return A RecipeBookDetailDto object that has been mapped from the provided RecipeBook object.
     */
    @Mapping(source = "editors", target = "users")
    RecipeBookDetailDto recipeBookToRecipeBookDetailDto(RecipeBook recipeBook);
}
