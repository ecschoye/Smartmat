package ntnu.idatt2106.backend.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.dto.ShoppingListElementDTO;
import ntnu.idatt2106.backend.model.enums.Role;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.requests.SaveGroceryRequest;
import ntnu.idatt2106.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final UserRepository userRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;
    private final GroceryRepository groceryRepository;
    private final GroceryShoppingCartRepository groceryShoppingCartRepository;

    private final CookieService cookieService;
    private final JwtService jwtService;
    private Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    public long createShoppingCart(long shoppingListId) {
        logger.info("Creating shopping cart for shopping list with id {}", shoppingListId);
        Optional<ShoppingList> shoppingList = shoppingListRepository.findById(shoppingListId);

        if (shoppingList.isEmpty()) {
            logger.info("Could not find a matching shopping list to shopping list id {}", shoppingListId);
            return -1;
        }

        logger.info("Found shopping list with id {}", shoppingList.get().getId());

        Optional<ShoppingCart> shoppingCartOptional = Optional.of(shoppingCartRepository.findShoppingListById(shoppingListId));
        if (shoppingCartOptional.isPresent()) {
            logger.info("Shopping cart already exists for shopping list with id {}", shoppingListId);
            return shoppingCartOptional.get().getId();
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setShoppingList(shoppingList.get());

        shoppingCartRepository.save(shoppingCart);
        logger.info("Created shopping cart with id {}", shoppingCart.getId());
        return shoppingCart.getId();
    }
    public List<ShoppingListElementDTO> getGroceries(long shoppingCartId) {
        logger.info("Retrieving groceries from the database");
        List<GroceryShoppingCart> groceries = shoppingCartRepository.findByShoppingCartId(shoppingCartId);
        if (groceries.isEmpty()) {
            logger.info("Received no groceries from the database");
        }
        List<ShoppingListElementDTO> dtos = groceries.stream().map(ShoppingListElementDTO::new).collect(Collectors.toList());
        logger.info("Received groceries from the database");
        return dtos;
    }

    // todo: is duplicate with method in ShoppingListService - remove
    private String extractEmail(HttpServletRequest httpRequest) {
        String token = cookieService.extractTokenFromCookie(httpRequest);
        return jwtService.extractClaim(token, Claims::getSubject);
    }

    private boolean isSuperUser(String eMail, long shoppingListId) {
        //find the refrigerator connected to the shoppingList
        Optional<Refrigerator> refrigerator = Optional.of(shoppingListRepository.findRefrigeratorById(shoppingListId));
        //find the role to the eMail in the refrigerator
        Optional<User> user = userRepository.findByEmail(eMail);

        if (user.isEmpty()) {
            logger.info("User is empty");
            return false;
        }

        Optional<RefrigeratorUser> refrigeratorUser = refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.get().getId(), refrigerator.get().getId());

        if (refrigeratorUser.isEmpty()) {
            logger.info("Refrigerator user is empty");
            return false;
        }

        logger.info("isUserSuper user {}", refrigeratorUser.get().getFridgeRole() == FridgeRole.SUPERUSER);
        return refrigeratorUser.get().getFridgeRole() == FridgeRole.SUPERUSER;
    }

    public Optional<GroceryShoppingCart> saveGrocery(SaveGroceryRequest groceryRequest, HttpServletRequest httpRequest) throws UnauthorizedException {
        String eMail = extractEmail(httpRequest);
        logger.info("Saving grocery: {} to shopping cart with id {}", groceryRequest.getName(), groceryRequest.getForeignKey());

        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(groceryRequest.getForeignKey());
        if (shoppingCart.isEmpty()) {
            logger.info("Could not find a shopping cart with id {}", groceryRequest.getForeignKey());
            return Optional.empty();
        }

        logger.info("Found shopping cart for shopping cart id {}", shoppingCart.get().getId());

        if (!isSuperUser(eMail, shoppingCart.get().getId())) {
            logger.info("You have to be super user to add a grocery to the shopping cart");
            throw new UnauthorizedException("It is only possible for super users to add grocery in shopping cart");
        }

        Optional<SubCategory> subCategory = subCategoryRepository.findById(groceryRequest.getSubCategoryId());
        if (subCategory.isEmpty()) {
            logger.info("Could not find a shopping list with id {}", groceryRequest.getForeignKey());
            return Optional.empty();
        }

        logger.info("Found subcategory with id {}", subCategory.get().getId());
        Grocery grocery = Grocery.builder()
                .name(groceryRequest.getName())
                .groceryExpiryDays(groceryRequest.getGroceryExpiryDays())
                .description(groceryRequest.getDescription())
                .subCategory(subCategory.get())
                .build();
        groceryRepository.save(grocery); //todo: what happens if i try to save the same grocery multiple times?
        logger.info("Created grocery with name {}", grocery.getName());

        GroceryShoppingCart groceryShoppingCart = new GroceryShoppingCart();
        groceryShoppingCart.setGrocery(grocery);
        groceryShoppingCart.setShoppingCart(shoppingCart.get());
        groceryShoppingCart.setQuantity(groceryRequest.getQuantity());

        logger.info("Saved new grocery to the grocery list");

        return Optional.of(groceryShoppingCartRepository.save(groceryShoppingCart));
    }
}
