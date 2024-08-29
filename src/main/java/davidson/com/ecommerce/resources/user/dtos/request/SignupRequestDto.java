package davidson.com.ecommerce.resources.user.dtos.request;

import jakarta.validation.constraints.NotNull;

public record SignupRequestDto(
        @NotNull(message = "Name is required")
        String name,

        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        String password
) {}
