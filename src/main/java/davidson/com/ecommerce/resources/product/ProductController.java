package davidson.com.ecommerce.resources.product;

import davidson.com.ecommerce.common.LinkBuilder;
import davidson.com.ecommerce.resources.product.dto.request.CreateProductRequestDto;
import davidson.com.ecommerce.resources.product.dto.request.UpdateProductRequestDto;
import davidson.com.ecommerce.resources.product.dto.response.GetProductResponseDto;
import davidson.com.ecommerce.resources.user.User;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
    private final LinkBuilder linkBuilder;

    public ProductController(ProductService productService, LinkBuilder linkBuilder) {
        this.productService = productService;
        this.linkBuilder = linkBuilder;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid CreateProductRequestDto dto) {
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
    public ResponseEntity<EntityModel<GetProductResponseDto>> getProduct(@PathVariable Long id) {
        Product product = productService.getdById(id);
        EntityModel<GetProductResponseDto> entityModel = EntityModel.of(GetProductResponseDto.fromEntity(product));
        entityModel.add(linkBuilder.linkToProducts().withRel("allProducts"));
        entityModel.add(linkBuilder.linkToUser(product.getRegisteredBy().getId()).withRel("admin"));
        return ResponseEntity.ok(entityModel);
    }


    @GetMapping
    public ResponseEntity<List<EntityModel<GetProductResponseDto>>> getAllProducts() {
        List<Product> products = productService.getAll();
        if (products.isEmpty()) return ResponseEntity.ok(List.of());
        List<EntityModel<GetProductResponseDto>> entityModels = products.stream().map(product -> {
            EntityModel<GetProductResponseDto> entityModel = EntityModel.of(GetProductResponseDto.fromEntity(product));
            entityModel.add(linkBuilder.linkToProduct(product.getId()).withSelfRel());
            entityModel.add(linkBuilder.linkToUser(product.getRegisteredBy().getId()).withRel("admin"));
            return entityModel;
        }).toList();
        return ResponseEntity.ok(entityModels);
    }


    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<GetProductResponseDto>> updateProduct(@PathVariable Long id, @RequestBody @Valid UpdateProductRequestDto dto) {
        Product product = productService.update(id, dto);
        EntityModel<GetProductResponseDto> entityModel = EntityModel.of(GetProductResponseDto.fromEntity(product));
        entityModel.add(linkBuilder.linkToProduct(product.getId()).withSelfRel());
        entityModel.add(linkBuilder.linkToUser(product.getRegisteredBy().getId()).withRel("admin"));
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
