package davidson.com.ecommerce.resources.product;

import davidson.com.ecommerce.resources.product.dto.request.CreateProductRequestDto;
import davidson.com.ecommerce.resources.product.dto.request.UpdateProductRequestDto;
import davidson.com.ecommerce.resources.product.dto.response.GetAllProductsResponseDto;
import davidson.com.ecommerce.resources.product.dto.response.GetProductResponseDto;
import davidson.com.ecommerce.resources.user.User;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<Void> createProduct(@RequestBody @Valid CreateProductRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();

        Product product = productService.save(dto, admin);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<GetProductResponseDto> getProduct(@PathVariable Long id) {
        Product product = productService.getdById(id);
        return ResponseEntity.ok(GetProductResponseDto.fromEntity(product));
    }


    @GetMapping
    @Cacheable("products")
    public ResponseEntity<List<GetAllProductsResponseDto>> getAllProducts() {
        List<Product> products = productService.getAll();
        if (products.isEmpty()) return ResponseEntity.ok(List.of());
        return ResponseEntity.ok(GetAllProductsResponseDto.fromEntities(products));
    }


    @PutMapping("/{id}")
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<GetProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody @Valid UpdateProductRequestDto dto) {
        Product product = productService.update(id, dto);
        return ResponseEntity.ok(GetProductResponseDto.fromEntity(product));
    }


    @DeleteMapping("/{id}")
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
