package ntnu.idatt2106.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Service for Session Storage.
 */
@Service
public class SessionStorageService {

    Logger logger = Logger.getLogger(SessionStorageService.class.getName());

    /**
     * Extracts JWT token from Authorization header.
     *
     * @param request http request
     * @return token
     */
    public String extractTokenFromAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        logger.info("No token found in Authorization header");
        return null;
    }
}
