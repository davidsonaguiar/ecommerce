package davidson.com.ecommerce.resources.user.dtos.response;

import davidson.com.ecommerce.resources.user.User;
import davidson.com.ecommerce.resources.user.enums.Role;
import jakarta.validation.constraints.NotNull;

public record SignupResponseDto(
        Long id,
        String name,
        String email,
        Role role
) {

    public static SignupResponseDto fromEntity(User user) {
        return new SignupResponseDto(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
