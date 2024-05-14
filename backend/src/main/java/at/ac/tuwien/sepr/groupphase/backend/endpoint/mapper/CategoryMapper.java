package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryResultDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {

    default CategoryResultDto categoryToCategoryResultDto(Category category){
         CategoryResultDto categoryResultDto = new CategoryResultDto();
         categoryResultDto.setId(category.getId());
         categoryResultDto.setName(category.getName());
         return categoryResultDto;
    }
}