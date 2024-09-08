package davidson.com.ecommerce.resources.sale_item.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record UpdateSaleItemDto(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be greater than 0")
        Integer quantity
) {
}
