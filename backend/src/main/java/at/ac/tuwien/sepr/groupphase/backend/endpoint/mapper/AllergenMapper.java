package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AllergenDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * This is the interface for the Allergen entities and related DTOs.
 *
 */
@Mapper
public interface AllergenMapper {
    AllergenDetailDto allergenToAllergenDetailDto(Allergen allergen);

    List<AllergenDetailDto> allergenListToAllergenDetailDtoList(List<Allergen> allergens);
}
