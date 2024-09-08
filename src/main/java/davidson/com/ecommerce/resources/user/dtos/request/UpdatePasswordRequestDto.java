package davidson.com.ecommerce.resources.user.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequestDto(
        @NotEmpty(message = "Password is required")
        @Size(min = 6, message = "Password must have at least 6 characters")
        String password
) {}
