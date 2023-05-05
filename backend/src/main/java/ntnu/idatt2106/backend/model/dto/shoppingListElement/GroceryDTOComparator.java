package ntnu.idatt2106.backend.model.dto.shoppingListElement;

import ntnu.idatt2106.backend.model.dto.GroceryDTO;
import java.util.Comparator;

public class GroceryDTOComparator implements Comparator<GroceryDTO>{

    @Override
    public int compare(GroceryDTO g1, GroceryDTO g2) {
        return g1.getDescription().compareTo(g2.getDescription());
    }
}
