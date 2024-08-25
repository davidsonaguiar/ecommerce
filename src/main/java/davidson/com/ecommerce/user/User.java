package davidson.com.ecommerce.user;

import davidson.com.ecommerce.user.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private Integer role;

    @ManyToOne
    @JoinColumn(name = "registered_by")
    private User registeredBy;

    public Role getRole() {
        return Role.fromInteger(role);
    }

    public void setRole(Role role) {
        this.role = role.getValue();
    }
}
