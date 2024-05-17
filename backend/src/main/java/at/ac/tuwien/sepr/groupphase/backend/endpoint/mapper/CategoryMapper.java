package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    CategoryDetailDto categoryToCategoryDetailDto(Category category);
}
