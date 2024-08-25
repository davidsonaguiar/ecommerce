package davidson.com.ecommerce.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequestDto(
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @Size(min = 3, max = 100, message = "Brand must be between 3 and 100 characters")
        String brand,

        @Size(min = 3, max = 100, message = "Model must be between 3 and 100 characters")
        String model,

        @Min(value = 0, message = "Price must be greater than 0")
        BigDecimal price
) {}
