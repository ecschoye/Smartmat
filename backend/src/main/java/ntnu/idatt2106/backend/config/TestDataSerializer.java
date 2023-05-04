package ntnu.idatt2106.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.enums.UserRole;
import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.SubCategory;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.enums.UserRole;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.recipe.RecipeCategory;
import ntnu.idatt2106.backend.model.recipe.RecipeGrocery;
import ntnu.idatt2106.backend.repository.*;
import ntnu.idatt2106.backend.repository.recipe.RecipeCategoryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeGroceryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Arrays;
import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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
@Profile("dev")
public class TestDataSerializer {

    private final CategoryRepository categoryRepository;

    private final SubCategoryRepository subCategoryRepository;

    private final GroceryRepository groceryRepository;

    private Category category;
    private SubCategory subCategory;

    private final RecipeCategoryRepository recipeCategoryRepository;

    private final RecipeRepository recipeRepository;

    private final RecipeGroceryRepository recipeGroceryRepository;

    private final UserRepository userRepository;

    private final RefrigeratorRepository refrigeratorRepository;

    private final RefrigeratorUserRepository refrigeratorUserRepository;

    private final RefrigeratorGroceryRepository refrigeratorGroceryRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UnitRepository unitRepository;

    @PostConstruct
    public void init() throws NumberFormatException {
        serialize();
        createUnits();


        createUser();
        createRefrigerator();

        //add groceries to refrigerator
        addGroceriesToRefrigerator();

        createRecipeCategories();
        createRecipes();
        createRecipeGroceries();
    }



    private void addGroceriesToRefrigerator() {
        Refrigerator refrigerator = refrigeratorRepository.findByName("Test Refrigerator")
                .orElseThrow(() -> new RuntimeException("Refrigerator not found: Test Refrigerator"));

        // Add groceries without the ids from the RecipeGrocery we looked at
        List<Long> groceryIds = List.of(10L, 63L, 96L, 119L, 126L, 147L, 182L, 329L, 364L, 464L, 597L, 692L, 718L, 756L, 798L, 890L, 908L);
        List<Long> unitIds = List.of(1L, 2L, 1L, 3L, 2L, 1L, 3L, 2L, 1L, 3L, 2L, 1L, 3L, 2L, 1L, 3L, 2L);
        insertRecipeGroceries(refrigerator, groceryIds, 1, unitIds);

        // Add groceries from the scenario above with their respective quantities
        List<Long> scenarioGroceryIds = List.of(95L, 153L, 798L, 870L, 320L);
        int[] quantities = {1, 3, 2, 1, 1}; // Updated quantities
        List<Long> scenarioUnitIds = List.of(2L, 1L, 3L, 2L, 1L);


        insertRecipeGroceries(refrigerator, scenarioGroceryIds, quantities, scenarioUnitIds);

    }

    private void insertRecipeGroceries(Refrigerator refrigerator, List<Long> groceryIds, int quantity, List<Long> unitIds) {
        for (int i = 0; i < groceryIds.size(); i++) {
            Long groceryId = groceryIds.get(i);
            Long unitId = unitIds.get(i);
            insertRecipeGrocery(refrigerator, groceryId, quantity, unitId);
        }
    }

    private void insertRecipeGroceries(Refrigerator refrigerator, List<Long> groceryIds, int[] quantities, List<Long> unitIds) {
        for (int i = 0; i < groceryIds.size(); i++) {
            Long groceryId = groceryIds.get(i);
            int quantity = quantities[i];
            Long unitId = unitIds.get(i);
            insertRecipeGrocery(refrigerator, groceryId, quantity, unitId);
        }
    }

    private void insertRecipeGrocery(Refrigerator refrigerator, long groceryId, int quantity, long unitId) {
        Grocery grocery = groceryRepository.findById(groceryId)
                .orElseThrow(() -> new RuntimeException("Grocery not found: " + groceryId));

        Unit unit = unitRepository.findById((unitId))
                .orElseThrow(() -> new RuntimeException("Unit not found: " + unitId));

        LocalDate physicalExpireDate = LocalDate.now().plusDays(grocery.getGroceryExpiryDays());

        for (int i = 0; i < quantity; i++) {
            // Check if the grocery already exists in the refrigerator
            if (!refrigeratorGroceryRepository.existsByRefrigeratorAndGrocery(refrigerator, grocery)) {
                RefrigeratorGrocery refrigeratorGrocery = RefrigeratorGrocery.builder()
                        .grocery(grocery)
                        .refrigerator(refrigerator)
                        .physicalExpireDate(physicalExpireDate)
                        .unit(unit)
                        .quantity(quantity)
                        .build();
                refrigeratorGroceryRepository.save(refrigeratorGrocery);
            }
        }
    }



    private void createRefrigerator() {
        String name = "Test Refrigerator";
        String address = "Test Address";

        if (!refrigeratorRepository.existsByName(name)) {
            Refrigerator refrigerator = refrigeratorRepository.save(Refrigerator.builder()
                    .name(name)
                    .address(address)
                    .build());

            User user = userRepository.findByEmail("test@test")
                    .orElseThrow(() -> new RuntimeException("User not found: test@test"));

            RefrigeratorUser refrigeratorUser = refrigeratorUserRepository.save(RefrigeratorUser.builder()
                    .refrigerator(refrigerator)
                    .user(user)
                    .fridgeRole(FridgeRole.SUPERUSER)
                    .build());
        }
    }

    private void createUser() {
        String name = "test";
        String email = "test@test";
        String password = "test";

        if (!userRepository.existsByEmail(email)) {
            userRepository.save(User.builder()
                    .name(name)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .userRole(UserRole.USER)
                    .build());
        }
    }


    private void createRecipeGroceries() {
        // An array of recipe names
        String[] recipeNames = {"Eggerøre", "Ostesmørbrød", "Pølse i brød", "Fisk"};

        // An array of grocery names
        long[] groceryNames = {153, 95, 798, 870, 320, 517};

        // An array of quantities for each recipe-grocery pair
        int[][] quantities = {
                {1, 0, 0, 0, 0, 0}, // Eggerøre needs one of 153
                {0, 2, 1, 0, 0, 0}, // Ostesmørbrød needs two of 95 and one of 79
                {0, 0, 0, 1, 1, 0},  // Pølse i brød needs one of 870 and one of 320
                {0, 0, 0, 0, 0, 1}
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
        String[] recipeNames = {"Eggerøre", "Ostesmørbrød", "Pølse i brød", "Fisk"};
        String[] recipeUrls = {
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsP-2AGz4oV39TsB-8_Fq2gtuPhdY2a9bg-g&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT63fKEnYFM1G0UdBFjhPYRjtPKEiooGZU7kA&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwZNzTD7pUbIO7zEnTSKwZgv0dOvyHiD5B8Q&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTAxgCKGhsBcpqf4EsCZ-i2A9VbcFnqajJv5w&usqp=CAU"
        };
        RecipeCategory[] categories = {
                getRecipeCategoryByName("Breakfast"),
                getRecipeCategoryByName("Lunch"),
                getRecipeCategoryByName("Dinner"),
                getRecipeCategoryByName("Dinner")
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

    private void createUnits(){
        if(unitRepository.findAll().size() == 0){
            unitRepository.save(Unit.builder().name("l")
                    .weight(1000)
                    .build());
            unitRepository.save(Unit.builder().name("kg")
                    .weight(1000)
                    .build());
            unitRepository.save(Unit.builder().name("g")
                    .weight(1)
                    .build());
            unitRepository.save(Unit.builder().name("dl")
                    .weight(100)
                    .build());
            unitRepository.save(Unit.builder().name("ml")
                    .weight(1)
                    .build());
            unitRepository.save(Unit.builder().name("hg")
                    .weight(100)
                    .build());
        }

    }

}


