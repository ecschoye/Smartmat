package ntnu.idatt2106.backend.service.recipe;


import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.RefrigeratorGrocery;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RefrigeratorGroceryRepository refrigeratorGroceryRepository;
    private final RecipeGroceryRepository recipeGroceryRepository;


    /**
     * Method to fetch recipes based on what the user has stored in their fridge
     * Also finds the recipes that uses groceries that are about to expire
     */
    public List<Recipe> getRecipesByGroceriesAndExpirationDates(long refrigeratorId) {

        //first fetch all groceries the user has in fridge
        List<RefrigeratorGrocery> allGroceries =  refrigeratorGroceryRepository.findAllByRefrigeratorId(refrigeratorId);

        // Filter groceries based on the expiration date to only include groceries that have not expired
        List<RefrigeratorGrocery> validGroceries = allGroceries.stream()
                .filter(item -> item.getPhysicalExpireDate().after(new Date())).toList();

        // retrieve all RecipeGrocery records that match the groceries in the validGroceries
        List<RecipeGrocery> matchingRecipeGroceries = recipeGroceryRepository.findAllByGroceryIn(
                validGroceries.stream().map(RefrigeratorGrocery::getGrocery).collect(Collectors.toList()));


        // Group the RecipeGrocery records by recipe and count the number of matched groceries for each recipe
        Map<Recipe, Long> recipeMatchCount = matchingRecipeGroceries.stream()
                .collect(Collectors.groupingBy(RecipeGrocery::getRecipe, Collectors.counting()));

        // Sort the recipes based on the number of matched groceries
        List<Recipe> sortedRecipes = recipeMatchCount.entrySet().stream()
                .sorted(Map.Entry.<Recipe, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey).toList();

        return sortedRecipes;
    }
}
