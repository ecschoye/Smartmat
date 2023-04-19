package ntnu.idatt2106.backend.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import ntnu.idatt2106.backend.exceptions.UserAlreadyExistsException;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.authentication.AuthenticationRequest;
import ntnu.idatt2106.backend.model.authentication.RegisterRequest;
import ntnu.idatt2106.backend.model.dto.response.AuthenticationResponse;
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
@Tag(name = "Login Controller", description = "Controller to handle user registration and authentication")
public class AuthenticateController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    Logger logger = Logger.getLogger(AuthenticateController.class.getName());

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws UserAlreadyExistsException {
        try {
            logger.info("Registering user " + request.getEmail());
            AuthenticationResponse authResponse = authenticationService.register(request);
            //HttpSession session = httpRequest.getSession();
            //session.setAttribute("SmartMatToken", authResponse.getToken());
            logger.info("User " + request.getEmail() + " registered successfully");
            return ResponseEntity.ok(authResponse);
        } catch (UserAlreadyExistsException e) {
            logger.info("User " + request.getEmail() + " already exists");
            throw new UserAlreadyExistsException("User " + request.getEmail() + " already exists");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest httpRequest) throws InvalidCredentialsException {
        try {
            logger.info("Authenticating user " + authenticationRequest.getEmail());
            AuthenticationResponse authResponse = authenticationService.authenticate(authenticationRequest);
            HttpSession session = httpRequest.getSession();
            session.setAttribute("SmartMatToken", authResponse.getToken());
            logger.info("Authentication successful");
            User user = userService.findByEmail(authenticationRequest.getEmail());
            authResponse.setUserId(user.getId());
            authResponse.setUserRole(user.getRole().toString());
            return ResponseEntity.ok(authResponse);
        } catch (InvalidCredentialsException e) {
            logger.info("Authentication failed");
            throw new InvalidCredentialsException("Email or password is incorrect.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest){
        logger.info("Logging out user");
        HttpSession session = httpRequest.getSession(false);
        if(session != null){
            session.removeAttribute("SmartMatToken");
            session.invalidate();
            logger.info("User logged out");
        }
        return ResponseEntity.ok("Logged out");
    }
}
