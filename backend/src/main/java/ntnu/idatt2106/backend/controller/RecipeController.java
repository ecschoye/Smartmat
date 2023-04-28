package ntnu.idatt2106.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.dto.MemberDTO;
import ntnu.idatt2106.backend.model.recipe.Recipe;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.service.recipe.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
@Tag(name = "Recipe controller", description = "Controller to handle the recipes")
public class RecipeController {

    private final Logger logger = Logger.getLogger(RecipeController.class.getName());
    private final RecipeService recipeService;


    @Operation(summary = "Fetch recipes based on available groceries and their expiration dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes fetched successfully", content = @Content(schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("{refrigeratorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> editRole(@Valid @PathVariable long refrigeratorId) throws Exception {
        logger.info("Received request to fetch recipes for user");
        try{
            List<Recipe> recipes = recipeService.getRecipesByGroceriesAndExpirationDates(refrigeratorId);
            return ResponseEntity.ok(recipes);
        }
        catch (Exception e){
            throw new Exception (e);
        }
    }
}
