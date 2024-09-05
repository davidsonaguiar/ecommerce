package davidson.com.ecommerce.resources.user.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequestDto(
        @NotNull(message = "Email is required")
        @Email(message = "Invalid email")
        String email
) {}
