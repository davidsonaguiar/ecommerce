package davidson.com.ecommerce.resources.product.dto.response;

import davidson.com.ecommerce.resources.category.Category;
import davidson.com.ecommerce.resources.product.Product;
import davidson.com.ecommerce.resources.product.ProductController;
import davidson.com.ecommerce.resources.user.dtos.response.IncludeUserResponseDto;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public record GetProductResponseDto(
        Long id,
        String name,
        String brand,
        String model,
        Double price,
        Integer quantity,
        Boolean active,
        List<Category> categories,
        IncludeUserResponseDto registeredBy,
        URI link
) {
    public static GetProductResponseDto fromEntity(Product product) {
        return new GetProductResponseDto(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getModel(),
                product.getPrice().doubleValue(),
                product.getQuantity(),
                product.getActive(),
                product.getCategories(),
                IncludeUserResponseDto.fromEntity(product.getRegisteredBy()),
                linkTo(methodOn(ProductController.class).getAllProducts()).toUri()
        );
    }
}
