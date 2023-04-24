package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.GroceryShoppingList;
import ntnu.idatt2106.backend.model.ShoppingCart;
import ntnu.idatt2106.backend.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findById(Long id);
    @Query(value = "SELECT gsc.grocery" +
            " FROM ShoppingCart sc, GroceryShoppingCart gsc, Grocery g" +
            " WHERE gsc.grocery.id = g.id AND sc.id = gsc.shoppingCart.id AND sc.id = :shoppingCartId")
    List<Grocery> findByShoppingCartId(@Param("shoppingCartId")Long shoppingCartId);//todo: edit
}
