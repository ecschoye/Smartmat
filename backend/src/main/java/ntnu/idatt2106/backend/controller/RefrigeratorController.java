package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.SaveException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.dto.MemberDTO;
import ntnu.idatt2106.backend.model.dto.RefrigeratorDTO;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.refrigerator.NewRefrigeratorDTO;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.model.requests.RemoveMemberRequest;
import ntnu.idatt2106.backend.service.CookieService;
import ntnu.idatt2106.backend.service.JwtService;
import ntnu.idatt2106.backend.service.RefrigeratorService;
import ntnu.idatt2106.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/refrigerator")
@RequiredArgsConstructor
@Tag(name = "Refrigerator Controller", description = "Controller to handle the refrigerator")
public class RefrigeratorController {

    private final RefrigeratorService refrigeratorService;

    private final CookieService cookieService;

    private final UserService userService;

    private final JwtService jwtService;

    Logger logger = LoggerFactory.getLogger(RefrigeratorController.class);


    @Operation(summary = "Edit role of a refrigerator member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role edited successfully", content = @Content(schema = @Schema(implementation = MemberDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/members/edit-role")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MemberDTO> editRole(@Valid @RequestBody MemberRequest memberRequest, HttpServletRequest httpRequest) {
        logger.info("Received request to edit member role in refrigerator");
        MemberDTO result;
        try {
            result = refrigeratorService.setFridgeRole(memberRequest, httpRequest);
            if (result == null) throw new Exception();
        } catch (Exception e) {
            logger.error("Could not edit role");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Returning response");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(summary = "Add a new member to a refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member added successfully", content = @Content(schema = @Schema(implementation = MemberDTO.class))),
            @ApiResponse(responseCode = "500", description = "Failed to add member")
    })
    @PostMapping("/members/new")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MemberDTO> newMember(@Valid @RequestBody MemberRequest memberRequest, HttpServletRequest httpRequest) throws SaveException {
        logger.info("Received request to add new member to refrigerator");
        MemberDTO result;
        try {
            result = refrigeratorService.addMember(memberRequest, httpRequest);
            if (result == null) throw new Exception();
        } catch (Exception e) {
            throw new SaveException("Failed to add member");
        }
        logger.info("Returning response");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(summary = "Remove a member from a refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member removed successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/members/remove")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> removeMember(@Valid @RequestBody RemoveMemberRequest memberRequest, HttpServletRequest httpRequest) throws Exception {
        logger.info("Received request to remove member from refrigerator");
        refrigeratorService.removeUserFromRefrigerator(memberRequest, httpRequest);
        logger.info("Member removed successfully");
        return new ResponseEntity<>(new SuccessResponse("Member removed successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @Operation(summary = "Create a new refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator created successfully", content = @Content(schema = @Schema(implementation = Refrigerator.class))),
            @ApiResponse(responseCode = "500", description = "Failed to create refrigerator")
    })
    @PostMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Refrigerator> newRefrigerator(@Valid @RequestBody NewRefrigeratorDTO refrigerator, HttpServletRequest httpRequest) throws SaveException {
        logger.info("Received request to create refrigerator for refrigerator");
        Refrigerator result;

        try {
            result = refrigeratorService.save(refrigerator, httpRequest);
            if (result == null) throw new Exception();
        } catch (Exception e) {
            throw new SaveException("Failed to create refrigerator");
        }
        logger.info("Returning refrigerator with id {}", result.getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Delete a refrigerator by ID and username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator deleted successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete/{refrigeratorId}/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> deleteRefrigerator(@Valid @PathVariable int refrigeratorId, HttpServletRequest httpRequest) throws Exception {
        refrigeratorService.forceDeleteRefrigerator(refrigeratorId, httpRequest);
        logger.info("Member removed successfully");
        return new ResponseEntity<>(new SuccessResponse("Member removed successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @Operation(summary = "Get all refrigerators by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of refrigerators fetched successfully", content = @Content(schema = @Schema(implementation = Refrigerator.class))),
            @ApiResponse(responseCode = "204", description = "User not found")
    })
    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Refrigerator>> getAllByUser(HttpServletRequest request) throws UserNotFoundException {
        logger.info("Received request for all refrigerators by user");
        List<Refrigerator> result = refrigeratorService.getAllByUser(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Get refrigerators by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of refrigerators fetched successfully", content = @Content(schema = @Schema(implementation = Refrigerator.class))),
            @ApiResponse(responseCode = "204", description = "No content")
    })
    @GetMapping("/{refrigeratorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RefrigeratorDTO> getById(@Valid @PathVariable long refrigeratorId, HttpServletRequest httpServletRequest) throws UserNotFoundException, RefrigeratorNotFoundException {
        logger.info("Received request for refrigerator with id: {}", refrigeratorId);

        RefrigeratorDTO result = refrigeratorService.getRefrigeratorDTOById(refrigeratorId, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
