package ntnu.idatt2106.backend.service.recipe;


import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.NoSuchElementException;
import ntnu.idatt2106.backend.model.dto.RecipeDTO;
import ntnu.idatt2106.backend.model.dto.recipe.FetchRecipesDTO;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import ntnu.idatt2106.backend.repository.RefrigeratorGroceryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeGroceryRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;
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
    public List<RecipeDTO> getRecipesByGroceriesAndExpirationDates(FetchRecipesDTO fetchRecipesDTO, boolean allRecipes) throws NoSuchElementException {

        long refrigeratorId = fetchRecipesDTO.getRefrigeratorId();
        int numRecipes = fetchRecipesDTO.getNumRecipes();
        List<Long> fetchedRecipeIds = fetchRecipesDTO.getFetchedRecipeIds();

        // First fetch all groceries the user has in fridge
        List<RefrigeratorGrocery> allGroceries = refrigeratorGroceryRepository.findAllByRefrigeratorId(refrigeratorId);

        if (allGroceries.isEmpty()) {
            throw new NoSuchElementException("No groceries found for the given refrigerator ID.");
        }

        // Filter groceries based on the expiration date to only include groceries that have not expired
        List<RefrigeratorGrocery> validGroceries = allGroceries.stream()
                .filter(item -> item.getPhysicalExpireDate().after(new Date())).toList();

        if (validGroceries.isEmpty()) {
            throw new NoSuchElementException("No valid groceries found. All groceries have expired.");
        }

        // Count the number of each grocery in the RefrigeratorGrocery table
        Map<Grocery, Integer> groceryCount = validGroceries.stream()
                .filter(item -> item.getRefrigerator().getId() == refrigeratorId)
                .collect(Collectors.groupingBy(RefrigeratorGrocery::getGrocery, Collectors.summingInt(item -> 1)));

        // Output the count for each grocery
        for (Map.Entry<Grocery, Integer> entry : groceryCount.entrySet()) {
            System.out.println(entry.getKey().getName() + entry.getKey().getId() + ": " + entry.getValue());
        }

        // Retrieve all RecipeGrocery records that match the groceries in the validGroceries
        List<RecipeGrocery> matchingRecipeGroceries = recipeGroceryRepository.findAllByGroceryIn(
                validGroceries.stream().map(RefrigeratorGrocery::getGrocery).collect(Collectors.toList()));

        if (matchingRecipeGroceries.isEmpty()) {
            throw new NoSuchElementException("No matching recipes found for the available groceries.");
        }

        // Group the RecipeGrocery records by recipe and count the number of matched groceries for each recipe
        Map<Recipe, Long> recipeMatchCount = matchingRecipeGroceries.stream()
                .filter(rg -> groceryCount.getOrDefault(rg.getGrocery(), 0) >= rg.getQuantity())
                .collect(Collectors.groupingBy(RecipeGrocery::getRecipe, Collectors.counting()));

        // Sort the recipes based on the number of matched groceries
        List<Recipe> sortedRecipes = recipeMatchCount.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()))
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        if (allRecipes) {
            return convertToDTOs(sortedRecipes);
        }

        // Exclude already fetched recipes from the sortedRecipes list
        List<Recipe> newRecipes = sortedRecipes.stream()
                .filter(recipe -> !fetchedRecipeIds.contains(recipe.getId()))
                .collect(Collectors.toList());

        // If there are not enough new recipes, fill the list with already fetched recipes
        if (newRecipes.size() < numRecipes) {
            List<Recipe> remainingRecipes = sortedRecipes.stream()
                    .filter(recipe -> fetchedRecipeIds.contains(recipe.getId()))
                    .collect(Collectors.toList());
            newRecipes.addAll(remainingRecipes.subList(0, numRecipes - newRecipes.size()));
        } else {
            newRecipes = newRecipes.subList(0, numRecipes);
        }
        return convertToDTOs(newRecipes);
    }




    public List<RecipeDTO> convertToDTOs(List<Recipe> recipes) {
        return recipes.stream().map(RecipeDTO::new).toList();
    }
}
