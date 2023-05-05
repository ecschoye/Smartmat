package ntnu.idatt2106.backend.config;

import jakarta.annotation.PostConstruct;
import jnr.constants.platform.Local;
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
import ntnu.idatt2106.backend.service.GroceryHistoryService;
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
@Profile("prod")
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

    private final GroceryHistoryRepository groceryHistoryRepository;
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
        createGroceryHistory();
    }



    private void addGroceriesToRefrigerator() {
        Refrigerator refrigerator = refrigeratorRepository.findByName("Test Refrigerator")
                .orElseThrow(() -> new RuntimeException("Refrigerator not found: Test Refrigerator"));

        // Add groceries without the ids from the RecipeGrocery we looked at
        List<Long> groceryIds = List.of(22L, 63L, 96L, 119L, 126L, 147L, 182L, 329L, 364L, 464L, 597L, 692L, 718L, 756L, 798L, 890L, 908L);
        List<Long> unitIds = List.of(1L, 2L, 1L, 3L, 2L, 1L, 3L, 2L, 1L, 3L, 2L, 1L, 3L, 2L, 1L, 3L, 2L);
        insertRecipeGroceries(refrigerator, groceryIds, 1, unitIds);
        // Add groceries from the scenario above with their respective quantities
        List<Long> scenarioGroceryIds = List.of(153L, 133L, 681L, 713L, 1945L, 1082L,95L, 153L, 798L, 870L, 320L,730L, 450L, 1534L, 1273L, 56L,154L, 1534L, 1475L, 1955L, 682L);
        int[] quantities = {120, 250, 150, 2, 8, 18,1, 3, 2, 1, 1,400, 400, 9, 200, 3,180, 250, 4, 1, 3}; // Updated quantities
        List<Long> scenarioUnitIds = List.of(3L, 3L, 3L, 4L, 3L, 3L,2L, 1L, 3L, 2L, 1L,3L, 3L, 3L, 3L, 4L,3L, 3L, 4L, 4L, 4L);

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

    private void createGroceryHistory() {
        Refrigerator refrigerator = refrigeratorRepository.findByName("Test Refrigerator")
                .orElseThrow(() -> new RuntimeException("Refrigerator not found: Test Refrigerator"));

        Random random = new Random();
        for (int i = 1; i <= 12; i++) {
            LocalDate date = LocalDate.now().minusMonths(i);

            for (int j = 0; j < 25; j++) {
                int weight = (j + 1) * 100;
                boolean wasTrashed = random.nextDouble() <= 0.2;

                Optional<GroceryHistory> groceryHistoryOptional = groceryHistoryRepository.findByDateConsumedAndWeightInGramsAndRefrigerator(date, weight, refrigerator);

                if (groceryHistoryOptional.isPresent()) {
                    // Data already exists, do nothing
                } else {
                    groceryHistoryRepository.save(GroceryHistory.builder()
                            .weightInGrams(weight)
                            .wasTrashed(wasTrashed)
                            .refrigerator(refrigerator)
                            .dateConsumed(date)
                            .build());
                }
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
        String[] recipeNames = {"Kremet pasta med laks", "Enkel sjokoladekake", "Grove vafler", "Hjemmelaget knekkebrød", "Protein-scones med cottage cheese"};

        HashMap<String, long[]> ingredientGroceries = new HashMap<>();
        HashMap<String, int[]> ingredientQuantities = new HashMap<>();
        HashMap<String, long[]> ingredientUnits = new HashMap<>();

        ingredientGroceries.put(recipeNames[0], new long[]{730L, 450L, 1534L, 1273L, 56L} ); // Grocery ID
        ingredientQuantities.put(recipeNames[0], new int[]{400, 400, 9, 200, 3} );    // Quantites
        ingredientUnits.put(recipeNames[0], new long[]{3L, 3L, 3L, 3L, 4L} );         // Unit

        ingredientGroceries.put(recipeNames[1], new long[]{154L, 1534L, 1475L, 1955L, 682L} ); // Grocery ID
        ingredientQuantities.put(recipeNames[1], new int[]{180, 250, 4, 1, 3} );    // Quantites
        ingredientUnits.put(recipeNames[1], new long[]{3L, 3L, 4L, 4L, 4L} );         // Unit

        ingredientGroceries.put(recipeNames[2], new long[]{1534L, 153L, 1475L, 22L, 682L, 681L, 1444L, 1945L} ); // Grocery ID
        ingredientQuantities.put(recipeNames[2], new int[]{36, 120, 18, 6, 2, 2, 6, 3} );    // Quantites
        ingredientUnits.put(recipeNames[2], new long[]{3L, 3L, 3L, 4L, 4L, 4L, 3L, 3L} );         // Unit

        ingredientGroceries.put(recipeNames[3], new long[]{681L, 714L, 1082L, 1081L, 1059L, 1617L} ); // Grocery ID
        ingredientQuantities.put(recipeNames[3], new int[]{4,4,2,1,1,5,7} );    // Quantites
        ingredientUnits.put(recipeNames[3], new long[]{4L,4L,4L,4L,4L,3L,4L} );         // Unit

        ingredientGroceries.put(recipeNames[4], new long[]{153L, 133L, 681L, 713L, 1945L, 1082L} ); // Grocery ID
        ingredientQuantities.put(recipeNames[4], new int[]{120, 250, 150, 2, 8, 18} );    // Quantites
        ingredientUnits.put(recipeNames[4], new long[]{3L, 3L, 3L, 4L, 3L, 3L} );         // Unit

        for (int i = 0; i < recipeNames.length; i++) {
            Recipe recipe = getRecipeByName(recipeNames[i]);
            long[] groceryIds = ingredientGroceries.get(recipeNames[i]);
            int[] quantities = ingredientQuantities.get(recipeNames[i]);
            long[] units = ingredientUnits.get(recipeNames[i]);

            for (int k = 0; k < groceryIds.length; k++) {
                Grocery grocery = getGroceryById(groceryIds[k]);
                Unit unit = getUnitById(units[k]);
                if (!recipeGroceryRepository.existsByRecipeAndGrocery(recipe, grocery)) {
                    recipeGroceryRepository.save(RecipeGrocery.builder()
                            .recipe(recipe)
                            .grocery(grocery)
                            .unit(unit)
                            .quantity(quantities[k])
                            .build());
                }
            }
        }
    }

    public Recipe getRecipeByName(String name) {
        return (Recipe) recipeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Recipe not found: " + name));
    }

    public Grocery getGroceryById(long id) {
        return groceryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grocery not found: " + id));
    }

    public Unit getUnitById(long id) {
        return unitRepository.findById(id)
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
        String[] recipeNames = {"Kremet pasta med laks", "Enkel sjokoladekake", "Grove vafler", "Hjemmelaget knekkebrød", "Protein-scones med cottage cheese"};
        String[] recipeUrls = {
                "https://www.tine.no/_/recipeimage/w_1600%2Ch_900%2Cc_fill%2Cx_2244%2Cy_1262%2Cg_xy_center/recipeimage/gojg6eaiym9ehbhd2b8l.jpg",
                "https://www.tine.no/_/recipeimage/w_1600%2Ch_900%2Cc_fill%2Cx_1500%2Cy_1000%2Cg_xy_center/recipeimage/317652.jpg",
                "https://www.tine.no/_/recipeimage/w_1600%2Ch_900%2Cc_fill%2Cx_1500%2Cy_1000%2Cg_xy_center/recipeimage/378532.jpg",
                "https://www.tine.no/_/recipeimage/w_1600%2Ch_900%2Cc_fill%2Cx_1500%2Cy_1097%2Cg_xy_center/recipeimage/357942.jpg",
                "https://www.tine.no/_/recipeimage/w_1600%2Ch_900%2Cc_fill%2Cx_1500%2Cy_937%2Cg_xy_center/recipeimage/427018.jpg"
        };
        RecipeCategory[] categories = {
                getRecipeCategoryByName("Dinner"),
                getRecipeCategoryByName("Dessert"),
                getRecipeCategoryByName("Dinner"),
                getRecipeCategoryByName("Lunch"),
                getRecipeCategoryByName("Lunch"),
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


    public RecipeCategory getRecipeCategoryByName(String name) {
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


