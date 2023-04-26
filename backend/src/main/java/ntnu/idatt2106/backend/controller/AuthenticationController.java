package ntnu.idatt2106.backend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import ntnu.idatt2106.backend.exceptions.UserAlreadyExistsException;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.authentication.AuthenticationRequest;
import ntnu.idatt2106.backend.model.authentication.RegisterRequest;
import ntnu.idatt2106.backend.model.dto.response.AuthenticationResponse;
import ntnu.idatt2106.backend.model.dto.response.RegisterResponse;
import ntnu.idatt2106.backend.service.AuthenticationService;
import ntnu.idatt2106.backend.service.UserService;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Controller to handle user registration and authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) throws UserAlreadyExistsException {
        try {
            logger.info("Registering user " + request.getEmail());
            RegisterResponse registerResponse = authenticationService.register(request);
            logger.info("User " + request.getEmail() + " registered successfully");
            return ResponseEntity.ok(registerResponse);
        } catch (UserAlreadyExistsException e) {
            logger.info("User " + request.getEmail() + " already exists");
            throw new UserAlreadyExistsException("Email" + request.getEmail() + " is already in use!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws InvalidCredentialsException {
        try {
            AuthenticationResponse authResponse = authenticationService.authenticate(authenticationRequest);

            Cookie accessTokenCookie = getCookie(authResponse);
            accessTokenCookie.setDomain("localhost");
            response.addCookie(accessTokenCookie);

            User user = userService.findByEmail(authenticationRequest.getEmail());
            authResponse.setUserId(user.getId());
            authResponse.setUserRole(user.getUserRole().toString());
            return ResponseEntity.ok(authResponse);
        } catch (InvalidCredentialsException e) {
            logger.info("Authentication failed");
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        logger.info("Logging out user");
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("SmartMatAccessToken")){
                    logger.info("Deleting cookie");
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        return ResponseEntity.ok("Logged out");
    }

    /**
     * Creates an access token as an HttpOnly cookie.
     *
     * @param authResponse AuthenticationResponse object containing the access token.
     * @return Cookie object containing the access token as an HttpOnly cookie.
     */
    public Cookie getCookie(AuthenticationResponse authResponse){
        // Set access token as an HttpOnly cookie
        Cookie accessTokenCookie = new Cookie("SmartMatAccessToken", authResponse.getToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(20 * 60); // 5 minutes

        return accessTokenCookie;
    }
}
