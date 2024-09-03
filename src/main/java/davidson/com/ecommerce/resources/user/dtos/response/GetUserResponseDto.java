package davidson.com.ecommerce.resources.user.dtos.response;

import java.util.List;

public record GetUserResponseDto (
        Long id,
        String name,
        String email,
        List<String> roles
){}
