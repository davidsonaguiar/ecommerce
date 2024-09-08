package davidson.com.ecommerce.resources.user.dtos.response;

import davidson.com.ecommerce.resources.user.User;
import davidson.com.ecommerce.resources.user.UserController;
import davidson.com.ecommerce.resources.user.enums.Role;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public record GetUserResponseDto (
        Long id,
        String name,
        String email,
        Role roles,
        URI link
){
    public static GetUserResponseDto fromEntity(User user) {
        return new GetUserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                linkTo(methodOn(UserController.class).getUser(user.getId())).toUri()
        );
    }

    public static List<GetUserResponseDto> fromEntities(List<User> users) {
        return users.stream().map(GetUserResponseDto::fromEntity).toList();
    }
}
