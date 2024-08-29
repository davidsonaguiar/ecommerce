package davidson.com.ecommerce.product;

import davidson.com.ecommerce.category.Category;
import davidson.com.ecommerce.category.CategoryRepository;
import davidson.com.ecommerce.exceptions.ContentConflictException;
import davidson.com.ecommerce.exceptions.ResourceNotFoundException;
import davidson.com.ecommerce.product.dto.request.CreateProductRequestDto;
import davidson.com.ecommerce.product.dto.request.UpdateProductRequestDto;
import davidson.com.ecommerce.user.User;
import davidson.com.ecommerce.user.UserRespository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRespository userRespository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, UserRespository userRespository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRespository = userRespository;
    }

    public Product save(CreateProductRequestDto dto) {
        Optional<Product> exists = productRepository.findByNameAndBrandAndModel(dto.name(), dto.brand(), dto.model());
        if (exists.isPresent()) throw new ContentConflictException("Product already exists");

        List<Category> categories = dto.categoriesIds()
                .stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId)))
                .toList();

        User admin = userRespository.findById(dto.adminId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + dto.adminId()));

        Product product = dto.toEntity();
        product.setCategories(categories);
        product.setRegisteredBy(admin);
        return productRepository.save(product);
    }

    public Product getdById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product update(Long id, UpdateProductRequestDto dto) {
        Product productToUpdate = getdById(id);

        Optional<Product> exists = productRepository.findByNameAndBrandAndModel(dto.name(), dto.brand(), dto.model());
        System.out.println(exists.get());
        if(exists.isPresent() && exists.get().getId() != id) throw new ContentConflictException("Product already exists");

        BeanUtils.copyProperties(dto, productToUpdate, "id");
        return productRepository.save(productToUpdate);
    }

    public void deleteById(Long id) {
        Product product = getdById(id);

        if(product.getSaleItems().size() == 0) {
            productRepository.deleteById(id);
            return;
        }

        product.setActive(false);
        productRepository.save(product);
    }
}
