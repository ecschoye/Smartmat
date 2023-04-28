package ntnu.idatt2106.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.Category;
import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.SubCategory;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeCategory;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import ntnu.idatt2106.backend.repository.CategoryRepository;
import ntnu.idatt2106.backend.repository.GroceryRepository;
import ntnu.idatt2106.backend.repository.SubCategoryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeCategoryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeGroceryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Class that serialises data into database from formatted csv file with categories, subcategories and groceries.
 * "!" indicates a category line, "?" indicates a subcategory, remaining lines are groceries.
 * Groceries are structured with name, description.
 * Subcategories are structured with name - expiry date.
 * Format example:
 *  !Dairy
 *  ?Milk - 14
 *  Chocolate Milk, 14% fat
 */
@Component
@RequiredArgsConstructor
public class TestDataSerializer {

    private final CategoryRepository categoryRepository;

    private final SubCategoryRepository subCategoryRepository;

    private final GroceryRepository groceryRepository;

    private Category category;
    private SubCategory subCategory;

    private final RecipeCategoryRepository recipeCategoryRepository;

    private final RecipeRepository recipeRepository;

    private final RecipeGroceryRepository recipeGroceryRepository;


    @PostConstruct
    public void init() throws IOException, NumberFormatException {
        serialize();
        createRecipeCategories();
        createRecipes();
        createRecipeGroceries();
    }

    private void createRecipeGroceries() {
        // An array of recipe names
        String[] recipeNames = {"Scrambled Eggs", "Grilled Cheese Sandwich", "Spaghetti Bolognese", "Chocolate Cake", "Fruit Salad"};

        // An array of grocery names
        long[] groceryNames = {9, 95, 153, 185, 217, 428};

        // An array of quantities for each recipe-grocery pair
        int[][] quantities = {
                {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 4, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
        };

        for (int i = 0; i < recipeNames.length; i++) {
            Recipe recipe = getRecipeByName(recipeNames[i]);

            for (int j = 0; j < groceryNames.length; j++) {
                if (quantities[i][j] > 0) {
                    Grocery grocery = getGroceryById(groceryNames[j]);

                    if (!recipeGroceryRepository.existsByRecipeAndGrocery(recipe, grocery)) {
                        recipeGroceryRepository.save(RecipeGrocery.builder()
                                .recipe(recipe)
                                .grocery(grocery)
                                .quantity(quantities[i][j])
                                .build());
                    }
                }
            }
        }
    }

    private Recipe getRecipeByName(String name) {
        return (Recipe) recipeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Recipe not found: " + name));
    }

    private Grocery getGroceryById(long id) {
        return groceryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grocery not found: " + id));
    }

    /**
     * Method that creates categories for recipes.
     */
    private void createRecipeCategories() {
        String[] categories = {"Breakfast", "Lunch", "Dinner", "Dessert", "Snack"};
        Arrays.stream(categories).forEach(category -> {
            if (!recipeCategoryRepository.existsByName(category)) {
                recipeCategoryRepository.save(RecipeCategory.builder()
                        .name(category).build());
            }
        });
    }


    private void createRecipes() {
        String[] recipeNames = {"Scrambled Eggs", "Grilled Cheese Sandwich", "Spaghetti Bolognese", "Chocolate Cake", "Fruit Salad"};
        String[] recipeUrls = {
                "https://www.example.com/recipes/scrambled-eggs",
                "https://www.example.com/recipes/grilled-cheese-sandwich",
                "https://www.example.com/recipes/spaghetti-bolognese",
                "https://www.example.com/recipes/chocolate-cake",
                "https://www.example.com/recipes/fruit-salad"
        };
        RecipeCategory[] categories = {
                getRecipeCategoryByName("Breakfast"),
                getRecipeCategoryByName("Lunch"),
                getRecipeCategoryByName("Dinner"),
                getRecipeCategoryByName("Dessert"),
                getRecipeCategoryByName("Snack")
        };

        for (int i = 0; i < recipeNames.length; i++) {
            if (!recipeRepository.existsByName(recipeNames[i])) {
                recipeRepository.save(Recipe.builder()
                        .name(recipeNames[i])
                        .url(recipeUrls[i])
                        .category(categories[i])
                        .build());
            }
        }
    }


    private RecipeCategory getRecipeCategoryByName(String name) {
        Optional<RecipeCategory> categories = recipeCategoryRepository.findByName(name);

        if (categories.isEmpty()) {
            throw new RuntimeException("Recipe category not found: " + name);
        }

        return categories.get();
    }



    /**
     * Method that reads csv file and calls methods for creating entities.
     */
    public void serialize(){
        category = new Category();
        subCategory = new SubCategory();

        if(categoryRepository.count() > 0 && subCategoryRepository.count() > 0 && groceryRepository.count() > 0){
            return;
        }
        String csvFile = "matvareliste.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                if(line.contains("!")){
                    createCategory(line);
                }
                else if(line.contains("?")){
                    createSubCategory(line);
                }
                else if(!line.isBlank()){
                    createGrocery(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }
    }

    private void createCategory(String line){
        line = line.replaceAll("\\!", "");
        category.setName(line);
        categoryRepository.save(Category.builder()
                .name(category.getName()).build());
        category = categoryRepository.findCategoryByName(category.getName()).orElseThrow();

    }
    private void createSubCategory(String line){
        line = line.replaceAll("\\?", "");
        subCategory.setCategory(category);
        String[] dataSplit = line.split("-");
        subCategory.setCategoryExpiryDays(Integer.parseInt(dataSplit[1].strip()));
        subCategory.setName(dataSplit[0]);
        subCategoryRepository.save(SubCategory.builder()
                .name(subCategory.getName())
                .category(subCategory.getCategory())
                .categoryExpiryDays(subCategory.getCategoryExpiryDays())
                .build());
        subCategory = subCategoryRepository.findSubCategoryByName(subCategory.getName()).orElseThrow();
    }
    private void createGrocery(String line){
        String[] csvData = line.split(",");
        Grocery grocery = new Grocery();
        grocery.setName(csvData[0]);
        String[] descArr = Arrays.copyOfRange(csvData, 0, csvData.length);
        String desc = String.join(", ",descArr);
        grocery.setDescription(desc);
        grocery.setSubCategory(subCategory);
        grocery.setGroceryExpiryDays(subCategory.getCategoryExpiryDays());
        groceryRepository.save(grocery);

    }

}


