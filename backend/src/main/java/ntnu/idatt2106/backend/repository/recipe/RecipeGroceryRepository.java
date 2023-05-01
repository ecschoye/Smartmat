package ntnu.idatt2106.backend.repository.recipe;

import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeGroceryRepository extends JpaRepository<RecipeGrocery, Long> {

    List<RecipeGrocery> findAllByGroceryIn(List<Grocery> groceries);
}
