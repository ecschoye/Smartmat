package ntnu.idatt2106.backend.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.dto.PasswordChangeDTO;
import ntnu.idatt2106.backend.model.dto.UserProfileDTO;
import ntnu.idatt2106.backend.model.dto.response.ErrorResponse;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.dto.response.UserStatusResponse;
import ntnu.idatt2106.backend.model.enums.AuthenticationState;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.service.AuthenticationService;
import ntnu.idatt2106.backend.service.JwtService;
import ntnu.idatt2106.backend.service.SessionStorageService;
import ntnu.idatt2106.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Login Controller", description = "Controller to handle user login")
public class MyProfileController {
    private final JwtService jwtService;
    private final UserService userService;

    private final SessionStorageService sessionStorageService;
    private final PasswordEncoder passwordEncoder;

    Logger logger = Logger.getLogger(MyProfileController.class.getName());

    /**
     * Retrieves the profile of the authenticated user.
     *
     * @param request the HTTP request containing the JWT token in the cookie
     * @return ResponseEntity containing the user's profile information
     */
    @Operation(summary = "Gets the profile of an authenticated user",
            description = "Retrieves the profile information of the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If authenticated, the authenticated user's profile information.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserProfileDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Returns error when authentication fails",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserProfileDTO.class)))
            })
    @GetMapping("/my-profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        try{

            User user = userService.findByEmail(jwtService.extractUsername(sessionStorageService.extractTokenFromAuthorizationHeader(request)));
            logger.info("Recieved request to get user profile on user: "+ user.getEmail() + ".");

            UserProfileDTO userProfileDTO = new UserProfileDTO(user.getName(), user.getEmail());
            logger.info("Returning user profile");
            return ResponseEntity.ok(userProfileDTO);
        }
        catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Token expired"));
        }
    }

    /**
     * Retrieves the authentication status of the user.
     *
     * @param request the HTTP request containing the JWT token in the cookie
     * @return ResponseEntity with status code 200 if the user is authenticated, 401 if the user is not authenticated
     */
    @Operation(summary = "Gets the authentication status of a user",
            description = "Retrieves the authentication status of the user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When user is authenticated.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationState.class))),
                    @ApiResponse(responseCode = "401", description = "When authentication fails.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationState.class))),
            })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user-status")
    public ResponseEntity<?> getUserStatus(HttpServletRequest request) {
        try {
            String jwt = sessionStorageService.extractTokenFromAuthorizationHeader(request); // Extract the token from the cookie
            if (jwt == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Unauthorized"));
            }
            User user = userService.findByEmail(jwtService.extractUsername(jwt)); // Pass the JWT token instead of the request
            AuthenticationState state = jwtService.getAuthenticationState(jwt, user);
            String role = user.getRole().toString();

            logger.info("Received request to get user status on user: "+ user.getEmail() + " " + user.getName() + ". User status: " + state + " Role: " + role);

            UserStatusResponse userStatusResponse = new UserStatusResponse(state, role);

            return ResponseEntity.ok(userStatusResponse);
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Token expired"));
        }
    }



    /**

     Handles a request to edit the user's name and email.

     @param userProfileDTO A DTO representing the user's updated name and email.

     @param request The HTTP request.

     @return A ResponseEntity containing either the updated UserProfileDTO if the request was successful, or an ErrorResponse if there was an error.
     */
    @Operation(summary = "Changes the details of a user.",
            description = "This method changes the email and/or name of a user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When password is changed successfully.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class))),
                    @ApiResponse(responseCode = "401", description = "When user is not authenticated.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/my-profile/edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> editMyProfile(@RequestBody UserProfileDTO userProfileDTO, HttpServletRequest request) {
        logger.info("Received request to edit profile; name: " + userProfileDTO.getName() + ", email: " + userProfileDTO.getEmail() + "");

        try {
            User user = userService.findByEmail(jwtService.extractUsername(sessionStorageService.extractTokenFromAuthorizationHeader(request)));
            user.setName(userProfileDTO.getName());
            user.setEmail(userProfileDTO.getEmail());
            userService.save(user);
            return ResponseEntity.ok(userProfileDTO);
        }
        catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Token expired"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Bad request"));
        }
    }


    /**

     Handles a request to change the user's password.

     @param passwordChangeDTO A DTO representing the user's old and new passwords.

     @param request The HTTP request.

     @return A ResponseEntity containing either a SuccessResponse if the password was changed successfully, or an ErrorResponse if there was an error.
     */
    @PostMapping("/my-profile/change-password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Changes the password of an authenticated user",
            description = "This endpoint changes the password of a user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "When password is changed successfully.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class))),
                    @ApiResponse(responseCode = "401", description = "When user is not authenticated.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO, HttpServletRequest request) {
        logger.info("Received request to change password for user: " + jwtService.extractUsername(sessionStorageService.extractTokenFromAuthorizationHeader(request)));

        try {
            User user = userService.findByEmail(jwtService.extractUsername(sessionStorageService.extractTokenFromAuthorizationHeader(request)));

            if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getPassword())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .header("error-message", "Old password is incorrect.")
                        .header("Access-Control-Expose-Headers", "error-message")
                        .build();
            }

            user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
            userService.save(user);

            return ResponseEntity.ok(new SuccessResponse("Password changed successfully", 200));
        }
        catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Token expired"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SuccessResponse("An error occurred while changing password", 500));
        }
    }



}