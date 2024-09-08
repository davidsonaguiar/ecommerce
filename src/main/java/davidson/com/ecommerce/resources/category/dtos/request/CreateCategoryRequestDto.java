package davidson.com.ecommerce.resources.category.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotNull(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 3 and 100 characters")
        String name
) {}
