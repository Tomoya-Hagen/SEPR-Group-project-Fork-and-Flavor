package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.IngredientResultDto;

import java.util.stream.Stream;

public interface IngredientService {

    /**
     * Find all ingredients having a name like name.
     *
     * @return limit amount of ingredients found
     */
    Stream<IngredientResultDto> byname(String name, int limit);
}
