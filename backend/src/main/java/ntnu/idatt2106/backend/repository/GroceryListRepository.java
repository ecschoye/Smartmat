package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Category;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.GroceryShoppingList;
import ntnu.idatt2106.backend.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GroceryListRepository extends JpaRepository<GroceryShoppingList, Long> {
    @Query(value = "SELECT gsl.grocery" +
            " FROM GroceryShoppingList gsl, Grocery g" +
            " WHERE gsl.grocery.id = g.id AND gsl.id = :shoppingListId")
    List<Grocery> findByShoppingListId(@Param("shoppingListId")Long shoppingListId);

    @Query(value = "SELECT gsl.grocery" +
            " FROM GroceryShoppingList gsl, Grocery g" +
            " WHERE gsl.grocery.id = g.id AND gsl.id = :shoppingListId AND g.subCategory.id = :subCategoryId")
    List<Grocery> findByShoppingListIdAndSubCategoryId(@Param("shoppingListId")Long shoppingListId, @Param("subCategoryId")Long subCategoryId);

    @Query(value = "SELECT DISTINCT sc.category" +
            " FROM GroceryShoppingList gsl, Grocery g, SubCategory sc" +
            " WHERE gsl.grocery.id = g.id AND gsl.id = :shoppingListId AND sc.id = g.subCategory.id")
    List<Category> findCategoryByShoppingListId(@Param("shoppingListId")Long shoppingListId);
}

