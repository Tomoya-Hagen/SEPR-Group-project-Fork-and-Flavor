package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryResultDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import org.mapstruct.Mapper;

/**
 * This mapper is used to map a category entity to a dto and reverse.
 */
@Mapper
public interface CategoryMapper {

    /** This method creates a CategoryDetailDto from a category entity.
     *
     * @param category represents the entity that should be converted to a dto.
     * @return an CategoryDetailDto which contains all important fields of a category entity.
     */
    CategoryDetailDto categoryToCategoryDetailDto(Category category);

    /**
     * This method creates a categoryResultDto from category entity.
     *
     * @param category category represents the entity that should be converted to a dto.
     * @return an CategoryResultDto which contains all important fields of a category entity.
     */
    default CategoryResultDto categoryToCategoryResultDto(Category category) {
        CategoryResultDto categoryResultDto = new CategoryResultDto();
        categoryResultDto.setId(category.getId());
        categoryResultDto.setName(category.getName());
        return categoryResultDto;
    }
}