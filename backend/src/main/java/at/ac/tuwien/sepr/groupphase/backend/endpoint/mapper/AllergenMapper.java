package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AllergenDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Allergen;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * This mapper is used to map an allergen entity to a dto and reverse.
 */
@Mapper
public interface AllergenMapper {

    /** This method creates an AllergenDetailDto from an allergen entity.
     *
     * @param allergen represents the entity that should be converted to a dto.
     * @return an AllergenDetailDto which contains all important fields of an allergen entity.
     */
    AllergenDetailDto allergenToAllergenDetailDto(Allergen allergen);

    /**
     * Maps a list of allergen to a list of allergenDetailDto dto.
     *
     * @param allergens that contains the items that should be mapped.
     * @return the list of the mapped objects.
     */

    List<AllergenDetailDto> allergenListToAllergenDetailDtoList(List<Allergen> allergens);
}
