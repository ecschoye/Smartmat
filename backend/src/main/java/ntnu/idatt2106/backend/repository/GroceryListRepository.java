package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.GroceryShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroceryListRepository extends JpaRepository<GroceryShoppingList, Long> {
    List<Grocery> findByShoppingListId(Long shoppingListId);
}
