package davidson.com.ecommerce.resources.user.dtos.response;

import davidson.com.ecommerce.resources.user.User;
import davidson.com.ecommerce.resources.user.UserController;
import davidson.com.ecommerce.resources.user.enums.Role;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public record GetAllUsersResponseDto (
        Long id,
        String name,
        URI link
){
    public static GetAllUsersResponseDto fromEntity(User user) {
        return new GetAllUsersResponseDto(
                user.getId(),
                user.getName(),
                linkTo(methodOn(UserController.class).getUser(user.getId())).toUri()
        );
    }

    public static List<GetAllUsersResponseDto> fromEntities(List<User> users) {
        return users.stream().map(GetAllUsersResponseDto::fromEntity).toList();
    }
}
