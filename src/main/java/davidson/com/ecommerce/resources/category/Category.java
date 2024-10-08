package davidson.com.ecommerce.resources.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import davidson.com.ecommerce.resources.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category: { " +
                "name = '" + name + '\'' +
                ", id= " + id +
                " }";
    }
}
