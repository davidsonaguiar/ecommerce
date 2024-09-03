package davidson.com.ecommerce.resources.product.dto.response;

import davidson.com.ecommerce.resources.category.Category;
import davidson.com.ecommerce.resources.product.Product;
import davidson.com.ecommerce.resources.user.dtos.response.IncludeUserResponseDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GetProductResponseDto(
        @NotNull(message = "Id is required")
        Long id,

        @NotNull(message = "Name is required")
        String name,

        @NotNull(message = "Brand is required")
        String brand,

        @NotNull(message = "Model is required")
        String model,

        @NotNull(message = "Price is required")
        Double price,

        @NotNull(message = "Quantity is required")
        Integer quantity,

        @NotNull(message = "Active is required")
        Boolean active,

        @NotEmpty(message = "Categories is required")
        List<Category> categories,

        IncludeUserResponseDto registeredBy
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
                IncludeUserResponseDto.fromEntity(product.getRegisteredBy())
        );
    }
}
