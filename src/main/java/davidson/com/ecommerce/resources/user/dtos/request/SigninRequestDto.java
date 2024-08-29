package davidson.com.ecommerce.resources.user.dtos.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
    @NotNull(message = "Email is required")
    String email,

    @NotNull(message = "Password is required")
    String password
) {}
