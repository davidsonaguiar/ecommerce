package davidson.com.ecommerce.resources.category;

import davidson.com.ecommerce.resources.category.dtos.request.CreateCategoryRequestDto;
import davidson.com.ecommerce.resources.category.dtos.response.GetAllCategoriesResponseDto;
import davidson.com.ecommerce.resources.category.dtos.response.GetCategoryResponseDto;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @CacheEvict(value = "categories", allEntries = true)
    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CreateCategoryRequestDto dto) {
        categoryService.createCategory(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.name())
                .toUri();
        return ResponseEntity.created(location).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<GetCategoryResponseDto> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok().body(GetCategoryResponseDto.fromEntity(category));
    }


    @Cacheable("categories")
    @GetMapping
    public ResponseEntity<List<GetAllCategoriesResponseDto>> getAllCategory() {
        System.out.println("Getting all categories");
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok().body(GetAllCategoriesResponseDto.fromEntities(categories));
    }


    @CacheEvict(value = "categories", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<GetCategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody @Valid CreateCategoryRequestDto dto) {
        Category category = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok().body(GetCategoryResponseDto.fromEntity(category));
    }


    @CacheEvict(value = "categories", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
