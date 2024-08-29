package davidson.com.ecommerce.resources.user.dtos.response;

import davidson.com.ecommerce.resources.user.User;
import davidson.com.ecommerce.resources.user.enums.Role;
import jakarta.validation.constraints.NotNull;

public record SignupResponseDto(
        @NotNull(message = "Id is required")
        Long id,

        @NotNull(message = "Name is required")
        String name,

        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Role is required")
        Role role
) {

    public static SignupResponseDto fromEntity(User user) {
        return new SignupResponseDto(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
