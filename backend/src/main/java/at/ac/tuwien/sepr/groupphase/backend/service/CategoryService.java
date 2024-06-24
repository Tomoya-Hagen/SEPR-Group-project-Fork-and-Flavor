package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryResultDto;

import java.util.stream.Stream;

/**
 * The CategoryService interface provides methods for retrieving categories.
 */
public interface CategoryService {

    /**
     * Retrieves a stream of CategoryResultDto entities that have a name like the given name. The results are limited by the given limit.
     *
     * @param name The string to search for in the names of the CategoryResultDto entities.
     * @param limit The maximum number of results to return.
     * @return A stream of CategoryResultDto entities that have a name like the given name, limited by the given limit.
     */
    Stream<CategoryResultDto> byname(String name, int limit);

    /**
     * Retrieves a stream of all CategoryResultDto entities.
     *
     * @return A stream of all CategoryResultDto entities.
     */
    Stream<CategoryResultDto> all();
}
