package ntnu.idatt2106.backend.service;

import ntnu.idatt2106.backend.exceptions.NoGroceriesFound;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.ShoppingCartNotFound;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.ShoppingList;
import ntnu.idatt2106.backend.repository.GroceryShoppingListRepository;
import ntnu.idatt2106.backend.repository.ShoppingListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ShoppingListServiceTest {
    @Mock
    private RefrigeratorService refrigeratorService;

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private GroceryShoppingListRepository groceryShoppingListRepository;

    @InjectMocks
    private ShoppingListService shoppingListService;

    @BeforeEach void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Does createShoppingList return the id to the shopping list connected to the refrigerator if it already is created")
    void test_create_shoppingList_for_refrigerator_when_shopping_list_already_is_created() throws RefrigeratorNotFoundException {
        long refrigeratorId = 1L;
        String refrigeratorName = "test";
        String refrigeratorAddress = "ntnu";
        Refrigerator refrigerator = Refrigerator.builder()
                .id(refrigeratorId)
                .name(refrigeratorName)
                .address(refrigeratorAddress)
                .build();
        ShoppingList shoppingList = ShoppingList.builder()
                .id(1L)
                .refrigerator(refrigerator)
                .build();

        when(refrigeratorService.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        when(shoppingListRepository.findByRefrigeratorId(refrigeratorId)).thenReturn(Optional.ofNullable(shoppingList));

        long resultId = shoppingListService.createShoppingList(refrigeratorId);

        assert shoppingList != null;
        assertEquals(resultId, shoppingList.getId());
    }

    @Test
    @DisplayName("Is it possible to create a shopping list if it does not already exits for the refrigerator")
    void test_create_new_shoppingList_for_refrigerator() throws RefrigeratorNotFoundException {
        long refrigeratorId = 1L;
        String refrigeratorName = "test";
        String refrigeratorAddress = "ntnu";
        Refrigerator refrigerator = Refrigerator.builder()
                        .id(refrigeratorId)
                        .name(refrigeratorName)
                        .address(refrigeratorAddress)
                        .build();
        ShoppingList shoppingList = ShoppingList.builder()
                        .refrigerator(refrigerator)
                        .build();

        when(refrigeratorService.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        when(shoppingListRepository.findByRefrigeratorId(refrigeratorId)).thenReturn(Optional.empty());
        when(shoppingListRepository.save(shoppingList)).thenReturn(shoppingList);

        long resultId = shoppingListService.createShoppingList(refrigeratorId);

        assertEquals(resultId, shoppingList.getId());
    }

    @Test
    @DisplayName("Throws getGroceries exception NoGroceriesFound when it is not any groceries in the database and is requested is true")
    void throws_getGroceries_NoGroceriesFound_when_no_groceries_in_the_database_and_isRequested_is_false() {
        long shoppingListId = 1L;
        boolean isRequested = false;
        when(groceryShoppingListRepository.findByShoppingListId(shoppingListId, isRequested)).thenReturn(new ArrayList<>());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.getGroceries(shoppingListId, isRequested));
    }

    @Test
    @DisplayName("Throws getGroceries exception NoGroceriesFound when it is not any groceries in the database and is requested is true")
    void throws_getGroceries_NoGroceriesFound_when_no_groceries_in_the_database_and_isRequested_is_true() {
        long shoppingListId = 1L;
        boolean isRequested = true;
        when(groceryShoppingListRepository.findByShoppingListId(shoppingListId, isRequested)).thenReturn(new ArrayList<>());

        assertThrows(NoGroceriesFound.class, () -> shoppingListService.getGroceries(shoppingListId, isRequested));
    }
}
