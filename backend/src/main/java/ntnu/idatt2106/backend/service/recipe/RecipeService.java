package ntnu.idatt2106.backend.service.recipe;


import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.NoSuchElementException;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.dto.RecipeDTO;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import ntnu.idatt2106.backend.repository.RefrigeratorGroceryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeGroceryRepository;
import ntnu.idatt2106.backend.service.GroceryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RefrigeratorGroceryRepository refrigeratorGroceryRepository;
    private final RecipeGroceryRepository recipeGroceryRepository;

    private final Logger logger = Logger.getLogger(RecipeService.class.getName());


    /**
     * Method to fetch recipes based on what the user has stored in their fridge
     * Also finds the recipes that uses groceries that are about to expire
     */
    public List<RecipeDTO> getRecipesByGroceriesAndExpirationDates(long refrigeratorId) throws NoSuchElementException {

        //first fetch all groceries the user has in fridge
        List<RefrigeratorGrocery> allGroceries = refrigeratorGroceryRepository.findAllByRefrigeratorId(refrigeratorId);
        logger.info("Found " + allGroceries.size() + " groceries for the given refrigerator ID.");

        if (allGroceries.isEmpty()) {
            throw new NoSuchElementException("No groceries found for the given refrigerator ID.");
        }

        // Filter groceries based on the expiration date to only include groceries that have not expired
        List<RefrigeratorGrocery> validGroceries = allGroceries.stream()
                .filter(item -> item.getPhysicalExpireDate().after(new Date())).toList();

        if (validGroceries.isEmpty()) {
            throw new NoSuchElementException("No valid groceries found. All groceries have expired.");
        }

        // retrieve all RecipeGrocery records that match the groceries in the validGroceries
        List<RecipeGrocery> matchingRecipeGroceries = recipeGroceryRepository.findAllByGroceryIn(
                validGroceries.stream().map(RefrigeratorGrocery::getGrocery).collect(Collectors.toList()));

        if (matchingRecipeGroceries.isEmpty()) {
            throw new NoSuchElementException("No matching recipes found for the available groceries.");
        }

        // Group the RecipeGrocery records by recipe and count the number of matched groceries for each recipe
        Map<Recipe, Long> recipeMatchCount = matchingRecipeGroceries.stream()
                .collect(Collectors.groupingBy(RecipeGrocery::getRecipe, Collectors.counting()));

        // Sort the recipes based on the number of matched groceries
        List<Recipe> sortedRecipes = recipeMatchCount.entrySet().stream()
                .sorted(Map.Entry.<Recipe, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey).toList();

        logger.info("Found " + sortedRecipes.size() + " recipes that match the available groceries.");
        logger.info("Recipes: " + sortedRecipes);

        return convertToDTOs(sortedRecipes);
    }


    public List<RecipeDTO> convertToDTOs(List<Recipe> recipes) {
        return recipes.stream().map(RecipeDTO::new).toList();
    }
}
