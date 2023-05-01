package ntnu.idatt2106.backend.repository.recipe;

import ntnu.idatt2106.backend.model.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Object> findByName(String name);

    boolean existsByName(String recipeName);
}