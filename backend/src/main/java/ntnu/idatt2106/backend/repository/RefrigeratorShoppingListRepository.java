package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.grocery.RefrigeratorShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RefrigeratorShoppingListRepository extends JpaRepository<RefrigeratorShoppingList, Long> {
    Optional<RefrigeratorShoppingList> findByGroceryIdAndShoppingListId(Long groceryId, Long shoppingListId);

    @Query(value = "SELECT rsl" +
            " FROM RefrigeratorShoppingList rsl, Grocery g" +
            " WHERE rsl.grocery.id = g.id AND rsl.shoppingList.id = :shoppingListId")
    List<RefrigeratorShoppingList> findByShoppingListId(@Param("shoppingListId")Long shoppingListId);
}
