package davidson.com.ecommerce.util;

import davidson.com.ecommerce.resources.category.Category;
import davidson.com.ecommerce.resources.category.CategoryRepository;
import davidson.com.ecommerce.resources.product.Product;
import davidson.com.ecommerce.resources.product.ProductRepository;
import davidson.com.ecommerce.resources.sale.Sale;
import davidson.com.ecommerce.resources.sale.SaleRepository;
import davidson.com.ecommerce.resources.sale_item.SaleItem;
import davidson.com.ecommerce.resources.user.User;
import davidson.com.ecommerce.resources.user.UserRespository;
import davidson.com.ecommerce.resources.user.enums.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Component
public class DataSeed implements CommandLineRunner {
    @Value("${active_seed}")
    private Boolean activeSeed;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final UserRespository userRespository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;

    public DataSeed(
            CategoryRepository categoryRepository,
            UserRespository userRespository,
            PasswordEncoder bCryptPasswordEncoder,
            ProductRepository productRepository,
            SaleRepository saleRepository) {
        this.categoryRepository = categoryRepository;
        this.userRespository = userRespository;
        this.passwordEncoder = bCryptPasswordEncoder;
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public void run(String... args) {
        if(activeSeed) {
            System.out.println("DataSeed running...");
            System.out.println();

            createCategories();
            createUsers();
            createProducts();
            createSales();
        }
    }

    private void createCategories() {
        try {
            List<String> categoriesName = List.of(
                    "Informática",
                    "Escritório",
                    "Vestuário",
                    "Alimentos e Bebidas",
                    "Esportes e Lazer",
                    "Eletrodomésticos",
                    "Automotivo",
                    "Brinquedos e Jogos",
                    "Saúde e Beleza",
                    "Móveis e Decoração"
            );

            categoriesName.forEach((name) -> {
                Category category = new Category(name);
                categoryRepository.save(category);
            });

            System.out.println("Success Created Categories");
            System.out.println();
        } catch (Exception exception) {
            System.out.println("Error Register Categorieis");
        }
    }

    private void createUsers() {
        try {
            String adminPassword = passwordEncoder.encode("admin1");
            String clientPassword = passwordEncoder.encode("client1");

            User admin1 = new User("Davidson Aguiar", "davidson.aguiar.pb@compasso.com.br", adminPassword, Role.ADMIN, true);
            User admin2 = new User("Admin Two", "admin2@example.com", adminPassword, Role.ADMIN, true);
            admin2.setRegisteredBy(admin1);

            User client1 = new User("Client One", "client1@example.com", clientPassword, Role.CLIENT, true);
            User client2 = new User("Client Two", "client2@example.com", clientPassword, Role.CLIENT, true);

            userRespository.saveAll(List.of(admin1, admin2, client1, client2));

            System.out.println("Success Created Users");
            System.out.println();
        } catch (Exception exception) {
            System.out.println("Error Register Users");
        }
    }

    private void createProducts() {
        try {
            System.out.println("Products already registers");
            Optional<User> admin1 = userRespository.findById(1L);
            Optional<User> admin2 = userRespository.findById(2L);

            if (admin1.isPresent() && admin2.isPresent()) {
                List<Category> categories = categoryRepository.findAll();

                Product product1 = new Product("Laptop", "Dell", "XPS 13", new BigDecimal("1500.00"), 10);
                product1.setRegisteredBy(admin1.get());
                product1.setCategories(categories.subList(0, 2));

                Product product2 = new Product("Smartphone", "Samsung", "Galaxy S21", new BigDecimal("800.00"), 20);
                product2.setRegisteredBy(admin2.get());
                product2.setCategories(categories.subList(2, 4));

                Product product3 = new Product("Headphones", "Sony", "WH-1000XM4", new BigDecimal("300.00"), 15);
                product3.setRegisteredBy(admin1.get());
                product3.setCategories(categories.subList(4, 6));

                Product product4 = new Product("Smartwatch", "Apple", "Watch Series 6", new BigDecimal("400.00"), 25);
                product4.setRegisteredBy(admin2.get());
                product4.setCategories(categories.subList(6, 8));

                productRepository.saveAll(List.of(product1, product2, product3, product4));

                System.out.println("Success Created Products");
                System.out.println();
            } else {
                System.out.println("Admin users not found.");
            }
        } catch (Exception exception) {
            System.out.println("Error Register Products");
        }
    }

    private void createSales() {
        try {
            Optional<User> client1 = userRespository.findById(3L);
            Optional<User> client2 = userRespository.findById(4L);

            if (client1.isPresent() && client2.isPresent()) {
                List<Product> products = productRepository.findAll();

                IntStream.range(0, 20).forEach(i -> {
                    Sale sale = new Sale();
                    sale.setSoldAt(getRandomDateTime());
                    sale.setSoldTo(i % 2 == 0 ? client1.get() : client2.get());

                    SaleItem saleItem = new SaleItem();
                    saleItem.getId().setProduct(products.get(i % products.size()));
                    saleItem.setQuantity(1);
                    saleItem.setPrice(products.get(i % products.size()).getPrice());
                    saleItem.getId().setSale(sale);

                    sale.setSaleItems(Set.of(saleItem));

                    saleRepository.save(sale);
                });

                System.out.println("Success Created Sales");
                System.out.println();
            } else {
                System.out.println("Client users not found.");
            }
        } catch (Exception exception) {
            System.out.println("Error Register Sales");
        }
    }

    public LocalDateTime getRandomDateTime() {
        LocalDateTime startDate = LocalDateTime.of(2024, Month.JUNE, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.now();

        long start = startDate.toEpochSecond(ZoneOffset.UTC);
        long end = endDate.toEpochSecond(ZoneOffset.UTC);

        long randomEpochSecond = ThreadLocalRandom.current().nextLong(start, end);
        return LocalDateTime.ofEpochSecond(randomEpochSecond, 0, ZoneOffset.UTC);
    }
}
