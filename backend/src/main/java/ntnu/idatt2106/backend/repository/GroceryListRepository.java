package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.GroceryShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroceryListRepository extends JpaRepository<GroceryShoppingList, Long> {
    @Query(value = "SELECT gsl.grocery" +
            " FROM GroceryShoppingList gsl, Grocery g" +
            " WHERE gsl.grocery.id = g.id AND gsl.id = :shoppingListId")
    List<Grocery> findByShoppingListId(@Param("shoppingListId")Long shoppingListId);
}
