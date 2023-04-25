package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Category;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.GroceryShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroceryShoppingListRepository extends JpaRepository<GroceryShoppingList, Long> {
    @Query(value = "SELECT gsl" +
            " FROM GroceryShoppingList gsl, Grocery g" +
            " WHERE gsl.grocery.id = g.id AND gsl.shoppingList.id = :shoppingListId")
    List<GroceryShoppingList> findByShoppingListId(@Param("shoppingListId")Long shoppingListId);

    @Query(value = "SELECT gsl.grocery" +
            " FROM GroceryShoppingList gsl, Grocery g" +
            " WHERE gsl.grocery.id = g.id AND gsl.shoppingList.id = :shoppingListId AND gsl.isRequest=true")
    List<Grocery> findRequestedGroceriesByShoppingListId(@Param("shoppingListId")Long shoppingListId);

    @Query(value = "SELECT gsl" +
            " FROM GroceryShoppingList gsl, Grocery g" +
            " WHERE gsl.grocery.id = g.id AND gsl.shoppingList.id = :shoppingListId AND g.subCategory.category.id = :categoryId")
    List<GroceryShoppingList> findByShoppingListIdAndCategoryId(@Param("shoppingListId")Long shoppingListId, @Param("categoryId")Long categoryId);

    @Query(value = "SELECT DISTINCT sc.category" +
            " FROM GroceryShoppingList gsl, Grocery g, SubCategory sc" +
            " WHERE gsl.grocery.id = g.id AND gsl.id = :shoppingListId AND sc.id = g.subCategory.id")
    List<Category> findCategoryByShoppingListId(@Param("shoppingListId")Long shoppingListId);
}

