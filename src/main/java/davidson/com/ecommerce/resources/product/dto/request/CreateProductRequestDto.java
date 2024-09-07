package davidson.com.ecommerce.resources.product.dto.request;

import davidson.com.ecommerce.resources.product.Product;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequestDto(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Brand is required")
        @Size(min = 2, max = 100, message = "Brand must be between 3 and 100 characters")
        String brand,

        @NotBlank(message = "Model is required")
        @Size(min = 1, max = 100, message = "Model must be between 1 and 100 characters")
        String model,

        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity must be greater than 0")
        Integer quantity,

        @NotEmpty(message = "Categories is required")
        List<Long> categoriesIds) {

    public Product toEntity() {
        return new Product(name, brand, model, price, quantity);
    }
}
