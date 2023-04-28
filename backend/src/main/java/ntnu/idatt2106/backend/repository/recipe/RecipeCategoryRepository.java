package ntnu.idatt2106.backend.repository.recipe;

import ntnu.idatt2106.backend.model.recipe.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {
    Optional<RecipeCategory> findByName(String name);
}