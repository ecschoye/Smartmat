package ntnu.idatt2106.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.category.Category;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.SubCategory;
import ntnu.idatt2106.backend.model.recipe.RecipeCategory;
import ntnu.idatt2106.backend.repository.CategoryRepository;
import ntnu.idatt2106.backend.repository.GroceryRepository;
import ntnu.idatt2106.backend.repository.SubCategoryRepository;
import ntnu.idatt2106.backend.repository.recipe.RecipeCategoryRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

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


    @PostConstruct
    public void init() throws IOException, NumberFormatException {
        serialize();
        createRecipeCategories();
    }

    /**
     * Method that creates categories for recipes.
     */
    private void createRecipeCategories() {
        String[] categories = {"Breakfast", "Lunch", "Dinner", "Dessert", "Snack"};
        Arrays.stream(categories).forEach(category -> {
            recipeCategoryRepository.save(RecipeCategory.builder()
                    .name(category).build());
        });
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


