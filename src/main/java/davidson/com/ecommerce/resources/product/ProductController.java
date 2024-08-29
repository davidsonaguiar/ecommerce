package davidson.com.ecommerce.product;

import davidson.com.ecommerce.product.dto.request.CreateProductRequestDto;
import davidson.com.ecommerce.product.dto.request.UpdateProductRequestDto;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Product> createProduct(@RequestBody @Valid CreateProductRequestDto dto) {
        Product product = productService.save(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> getProduct(@PathVariable Long id) {
        Product product = productService.getdById(id);
        EntityModel<Product> entityModel = EntityModel.of(product);
        WebMvcLinkBuilder linkToProducts = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllProducts());
        entityModel.add(linkToProducts.withRel("allProducts"));
        return ResponseEntity.ok(entityModel);
    }


    @GetMapping
    public ResponseEntity<List<EntityModel<Product>>> getAllProducts() {
        List<Product> products = productService.getAll();
        if (products.isEmpty()) return ResponseEntity.ok(List.of());
        List<EntityModel<Product>> entityModels = products.stream().map(product -> {
            EntityModel<Product> entityModel = EntityModel.of(product);
            WebMvcLinkBuilder linkToProduct = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getProduct(product.getId()));
            entityModel.add(linkToProduct.withRel("product"));
            return entityModel;
        }).toList();
        return ResponseEntity.ok(entityModels);
    }


    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> updateProduct(@PathVariable Long id, @RequestBody @Valid UpdateProductRequestDto dto) {
        Product product = productService.update(id, dto);
        EntityModel<Product> entityModel = EntityModel.of(product);
        WebMvcLinkBuilder linkToAllProducts = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllProducts());
        entityModel.add(linkToAllProducts.withRel("allProducts"));
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
