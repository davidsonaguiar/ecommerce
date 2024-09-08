package davidson.com.ecommerce.resources.category;

import davidson.com.ecommerce.exceptions.ContentConflictException;
import davidson.com.ecommerce.exceptions.ResourceNotFoundException;
import davidson.com.ecommerce.resources.category.dtos.request.CreateCategoryRequestDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public void createCategory(CreateCategoryRequestDto dto) throws ContentConflictException {
        Optional<Category> exists = categoryRepository.findByName(dto.name());
        if(exists.isPresent()) throw new ContentConflictException("Category already exists");
        Category category = new Category(dto.name().toLowerCase().trim());
        categoryRepository.save(category);
    }


    public Category getCategory(Long id) throws ResourceNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) throw new ResourceNotFoundException("Category not found");
        return category.get();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Long id, CreateCategoryRequestDto dto) throws ResourceNotFoundException, ContentConflictException {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) throw new ResourceNotFoundException("Category not found");
        if(category.get().getName().equals(dto.name().toLowerCase().trim())) return category.get();

        Optional<Category> exists = categoryRepository.findByName(dto.name());
        if(exists.isPresent()) throw new ContentConflictException("Category already exists");

        Category categoryToUpdate = category.get();
        categoryToUpdate.setName(dto.name().toLowerCase().trim());

        return categoryRepository.save(categoryToUpdate);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
