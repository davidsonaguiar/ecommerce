package davidson.com.ecommerce.resources.user.dtos.response;

import davidson.com.ecommerce.resources.user.User;
import davidson.com.ecommerce.resources.user.UserController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public record IncludeUserResponseDto(Long id, String name, URI link) {
    public static IncludeUserResponseDto fromEntity(User user) {
        if (user == null) return null;
        return new IncludeUserResponseDto(
                user.getId(),
                user.getName(),
                linkTo(methodOn(UserController.class).getUser(user.getId())).toUri()
        );
    }
}
