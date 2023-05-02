package ntnu.idatt2106.backend.model.dto.shoppingCartElement;


import java.util.Comparator;

public class ShoppingCartElementDTOComparator implements Comparator<ShoppingCartElementDTO> {

    @Override
    public int compare(ShoppingCartElementDTO s1, ShoppingCartElementDTO s2) {
        return s1.getDescription().compareTo(s2.getDescription());
    }
}
