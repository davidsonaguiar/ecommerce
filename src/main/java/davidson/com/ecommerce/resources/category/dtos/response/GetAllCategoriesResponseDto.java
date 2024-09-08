package davidson.com.ecommerce.resources.category.dtos.response;

import davidson.com.ecommerce.resources.category.Category;
import davidson.com.ecommerce.resources.category.CategoryController;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public record GetAllCategoriesResponseDto(
        Long id,
        String name,
        URI link
) {
    public static GetAllCategoriesResponseDto fromEntity(Category category) {
        return new GetAllCategoriesResponseDto(
                category.getId(),
                category.getName(),
                linkTo(methodOn(CategoryController.class).getAllCategory()).toUri()
        );
    }

    public static List<GetAllCategoriesResponseDto> fromEntities(List<Category> categories) {
        return categories.stream().map(GetAllCategoriesResponseDto::fromEntity).toList();
    }
}
