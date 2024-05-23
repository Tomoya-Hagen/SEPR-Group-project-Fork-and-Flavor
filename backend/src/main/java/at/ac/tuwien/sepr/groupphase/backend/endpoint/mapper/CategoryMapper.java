package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import org.mapstruct.Mapper;

/**
 * This is the interface for the Category entities and related DTOs.
 *
 */
@Mapper
public interface CategoryMapper {

    /**
     * This function maps a Category entity to a CategoryDetailDto.
     *
     * @param category The category entity to map.
     * @return The mapped result.
     */
    CategoryDetailDto categoryToCategoryDetailDto(Category category);
}
