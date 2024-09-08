package davidson.com.ecommerce.resources.category.dtos.response;

import davidson.com.ecommerce.resources.category.Category;
import davidson.com.ecommerce.resources.category.CategoryController;
import davidson.com.ecommerce.resources.product.dto.response.GetAllProductsResponseDto;

import java.util.List;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public record GetCategoryResponseDto(
        Long id,
        String name,
        List<GetAllProductsResponseDto> products,
        URI link
) {
    public static GetCategoryResponseDto fromEntity(Category category) {
        return new GetCategoryResponseDto(
                category.getId(),
                category.getName(),
                GetAllProductsResponseDto.fromEntities(category.getProducts()),
                linkTo(methodOn(CategoryController.class).getAllCategory()).toUri()
        );
    }
}
