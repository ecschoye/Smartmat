package ntnu.idatt2106.backend.model.dto.shoppingListElement;

import java.util.Comparator;

public class ShoppingListElementDTOComparator implements Comparator<ShoppingListElementDTO> {
    @Override
    public int compare(ShoppingListElementDTO s1, ShoppingListElementDTO s2) {
        return s1.getDescription().compareTo(s2.getDescription());
    }
}
