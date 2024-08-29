package davidson.com.ecommerce.resources.user.dtos.response;

import jakarta.validation.constraints.NotNull;

public record SigninResponseDto(
        @NotNull(message = "Token is required")
        String token
) {}
