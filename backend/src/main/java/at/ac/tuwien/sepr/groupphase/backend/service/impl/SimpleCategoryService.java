package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.CategoryResultDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.CategoryMapper;
import at.ac.tuwien.sepr.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.CategoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class SimpleCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public SimpleCategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Stream<CategoryResultDto> byname(String name, int limit) {
        var x = categoryRepository.findByNameContainingIgnoreCase(name, PageRequest.of(0, limit));
        return x.stream().map(categoryMapper::categoryToCategoryResultDto);
    }

    @Override
    public Stream<CategoryResultDto> all() {
        var x = categoryRepository.findAll();
        return x.stream().map(categoryMapper::categoryToCategoryResultDto);
    }
}
