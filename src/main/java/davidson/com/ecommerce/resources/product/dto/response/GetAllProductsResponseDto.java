package davidson.com.ecommerce.resources.product.dto.response;

import davidson.com.ecommerce.resources.product.Product;
import davidson.com.ecommerce.resources.product.ProductController;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public record GetAllProductsResponseDto(
        Long id,
        String name,
        String brand,
        String model,
        BigDecimal price,
        URI link) {

    public static GetAllProductsResponseDto fromEntity(Product product) {
        return new GetAllProductsResponseDto(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getModel(),
                product.getPrice(),
                linkTo(methodOn(ProductController.class).getProduct(product.getId())).toUri()
        );
    }

    public static List<GetAllProductsResponseDto> fromEntities(List<Product> products) {
        return products.stream().map(GetAllProductsResponseDto::fromEntity).toList();
    }
}
