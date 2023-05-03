package ntnu.idatt2106.backend.repository.recipe;

import ntnu.idatt2106.backend.model.dto.GroceryInfoDTO;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeGroceryRepository extends JpaRepository<RecipeGrocery, Long> {

    List<RecipeGrocery> findAllByGroceryIn(List<Grocery> groceries);

    boolean existsByRecipeAndGrocery(Recipe recipe, Grocery grocery);

    List<RecipeGrocery> findAllByRecipe(Recipe recipe);

    @Query("SELECT new ntnu.idatt2106.backend.model.dto.GroceryInfoDTO(g.id, g.name) FROM RecipeGrocery rg JOIN rg.grocery g WHERE rg.recipe = :recipe")
    List<GroceryInfoDTO> findGroceryInfoByRecipe(Recipe recipe);

    Optional<RecipeGrocery> findFirstByRecipeAndGrocery(Recipe recipe, Grocery grocery);
}
