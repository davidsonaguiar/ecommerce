package davidson.com.ecommerce.resources.product;

import davidson.com.ecommerce.resources.sale_item.SaleItem;
import davidson.com.ecommerce.resources.category.Category;
import davidson.com.ecommerce.resources.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Brand is required")
    @Size(min = 2, max = 100, message = "Brand must be between 3 and 100 characters")
    @Column(nullable = false)
    private String brand;

    @NotBlank(message = "Model is required")
    @Size(min = 3, max = 100, message = "Model must be between 3 and 100 characters")
    @Column(nullable = false)
    private String model;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than 0")
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater than 0")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Active is required")
    @Column(nullable = false)
    private Boolean active;

    @NotNull(message = "Category is required")
    @ManyToMany
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "id.product")
    private List<SaleItem> saleItems = new ArrayList<>();

    @NotNull(message = "Registered by is required")
    @ManyToOne
    @JoinColumn(name = "registered_by")
    private User registeredBy;

    public Product(String name, String brand, String model, BigDecimal price, Integer quantity) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.quantity = quantity;
        this.active = true;
    }
}
