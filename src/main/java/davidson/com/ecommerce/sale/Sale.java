package davidson.com.ecommerce.sale;

import davidson.com.ecommerce.SaleItem.SaleItem;
import davidson.com.ecommerce.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sales")
public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime soldAt;

    @ManyToOne
    @JoinColumn(name = "sold_to")
    private User soldTo;

    @OneToMany(mappedBy = "id.sale")
    private Set<SaleItem> saleItems = new HashSet<>();
}
