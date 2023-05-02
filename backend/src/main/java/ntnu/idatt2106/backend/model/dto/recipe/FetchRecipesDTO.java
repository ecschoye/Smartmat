package ntnu.idatt2106.backend.model.dto.recipe;

import lombok.Data;

import java.util.List;


@Data
public class FetchRecipesDTO {


    private long refrigeratorId;
    private int numRecipes;
    private List<Long> fetchedRecipeIds;
}
