package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.grocery.GroceryShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query(value = "SELECT gsc" +
            " FROM ShoppingCart sc, GroceryShoppingCart gsc, Grocery g" +
            " WHERE gsc.grocery.id = g.id AND sc.id = gsc.shoppingCart.id AND sc.id = :shoppingCartId")
    List<GroceryShoppingCart> findByShoppingCartId(@Param("shoppingCartId")Long shoppingCartId);
    Optional<ShoppingCart> findByShoppingListId(Long id);

    void removeByShoppingList(ShoppingList shoppingList);
}
