package campusfind.demo.service;

import campusfind.demo.entity.Category;
import campusfind.demo.exception.BadRequestException;
import campusfind.demo.exception.ResourceNotFoundException;
import campusfind.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name, String description) {
        String cleanName = name != null ? name.trim() : "";
        if (categoryRepository.existsByNameIgnoreCase(cleanName)) {
            throw new BadRequestException("Category with name '" + cleanName + "' already exists.");
        }
        Category category = new Category(cleanName, description != null ? description.trim() : "");
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
