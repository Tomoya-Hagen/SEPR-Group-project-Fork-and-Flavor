package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryResultDto;

import java.util.stream.Stream;

public interface CategoryService {
    /**
     * Find all categories having a name like name.
     *
     * @return limit amount of categories found
     */
    Stream<CategoryResultDto> byname(String name, int limit);

    /**
     * Find all categories.
     *
     * @return all categories found
     */
    Stream<CategoryResultDto> all();
}
