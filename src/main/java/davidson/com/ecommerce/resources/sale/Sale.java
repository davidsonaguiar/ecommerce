package davidson.com.ecommerce.resources.sale;

import davidson.com.ecommerce.resources.sale_item.SaleItem;
import davidson.com.ecommerce.resources.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sales")
public class Sale extends RepresentationModel<Sale> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime soldAt;

    @ManyToOne
    @JoinColumn(name = "sold_to")
    private User soldTo;

    @OneToMany(mappedBy = "id.sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SaleItem> saleItems = new HashSet<>();

    public BigDecimal getTotalValue() {
        return saleItems.stream()
                .map(SaleItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Sale: { " +
                "id = " + id +
                ", soldAt = " + soldAt +
                ", soldTo = " + soldTo +
                ", saleItems = " + saleItems +
                " }";
    }
}
