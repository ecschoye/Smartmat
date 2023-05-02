package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroceryShoppingCartRepository extends JpaRepository<GroceryShoppingCart, Long> {
}
