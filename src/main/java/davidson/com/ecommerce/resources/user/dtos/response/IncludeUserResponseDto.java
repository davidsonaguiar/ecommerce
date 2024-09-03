package davidson.com.ecommerce.resources.user.dtos.response;

import davidson.com.ecommerce.resources.user.User;
import jakarta.validation.constraints.NotNull;

public record IncludeUserResponseDto(
        @NotNull(message = "Id is required")
        Long id,

        @NotNull(message = "Name is required")
        String name
) {

    public static IncludeUserResponseDto fromEntity(User user) {
        if (user == null) return null;
        return new IncludeUserResponseDto(
                user.getId(),
                user.getName()
        );
    }
}
