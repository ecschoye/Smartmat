package ntnu.idatt2106.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

/**
 * Service for Session Storage.
 */
@Service
public class SessionStorageService {

    /**
     * Extracts token from HttpSession
     * @param request http requst
     * @return token
     */
    public String extractTokenFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object token = session.getAttribute("SmartMatToken");
            if (token != null) {
                return token.toString();
            }
        }
        return null;
    }
}
