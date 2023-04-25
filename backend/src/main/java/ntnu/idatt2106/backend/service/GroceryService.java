package ntnu.idatt2106.backend.service;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.dto.RefrigeratorGroceryDTO;
import ntnu.idatt2106.backend.model.enums.Role;
import ntnu.idatt2106.backend.repository.RefrigeratorGroceryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling operations regarding groceries inside a refrigerator
 */
@Service
@RequiredArgsConstructor
public class GroceryService {

    private final Logger logger = LoggerFactory.getLogger(GroceryService.class);

    private final CookieService cookieService;
    private final JwtService jwtService;

    private final RefrigeratorGroceryRepository refGroceryRepository;
    private final RefrigeratorService refrigeratorService;

    /**
     * Gets a list of DTO's representing groceries
     * in refrigerator.
     *
     * @param refrigeratorId Refrigerator id
     * @param request Http request
     * @return List of RefrigeratorGrocery DTO's
     * @throws RefrigeratorNotFoundException If refrigerator not found
     * @throws UserNotFoundException if user not found
     * @throws UnauthorizedException if user not member
     */
    public List<RefrigeratorGroceryDTO> getGroceriesByRefrigerator(long refrigeratorId, HttpServletRequest request) throws RefrigeratorNotFoundException, UserNotFoundException, UnauthorizedException {
        //Throws if refrigerator does not exist
        Refrigerator refrigerator = refrigeratorService.getRefrigerator(refrigeratorId);
        logger.debug("getting username");
        String email = extractEmail(request);
        //Throws if user is not member
        logger.debug("getting user role");
        Role userRole = refrigeratorService.getUserRole(refrigerator, email);

        logger.debug("getting groceries");
        List<RefrigeratorGrocery> groceries = refGroceryRepository.findByRefrigerator(refrigerator);
        List<RefrigeratorGroceryDTO> refGroceryDTOS = new ArrayList<>();
        for (RefrigeratorGrocery grocery: groceries) {
            refGroceryDTOS.add(new RefrigeratorGroceryDTO(grocery));
        }
        return refGroceryDTOS;
    }

    /**
     * Extracts username from cookie
     * @param httpRequest Request we are extracting from
     * @return username
     */
    public String extractEmail(HttpServletRequest httpRequest) {
        String token = cookieService.extractTokenFromCookie(httpRequest);
        return jwtService.extractClaim(token, Claims::getSubject);
    }
}
