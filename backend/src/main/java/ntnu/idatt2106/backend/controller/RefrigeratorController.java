package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.LastSuperuserException;
import ntnu.idatt2106.backend.exceptions.SaveException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.dto.refrigerator.RefrigeratorDTO;
import ntnu.idatt2106.backend.model.dto.response.RefrigeratorResponse;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.model.dto.response.MemberResponse;
import ntnu.idatt2106.backend.model.requests.RemoveMemberRequest;
import ntnu.idatt2106.backend.service.CookieService;
import ntnu.idatt2106.backend.service.JwtService;
import ntnu.idatt2106.backend.service.RefrigeratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import java.nio.file.AccessDeniedException;
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

    private final JwtService jwtService;

    Logger logger = LoggerFactory.getLogger(RefrigeratorController.class);


    @Operation(summary = "Edit role of a refrigerator member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role edited successfully", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/members/edit-role")
    public ResponseEntity<MemberResponse> editRole(@Valid @RequestBody MemberRequest memberRequest) {
        logger.info("Received request to edit member role in refrigerator");
        MemberResponse result;
        try {
            result = refrigeratorService.setFridgeRole(memberRequest);
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
            @ApiResponse(responseCode = "200", description = "Member added successfully", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "500", description = "Failed to add member")
    })
    @PostMapping("/members/new")
    public ResponseEntity<MemberResponse> newMember(@Valid @RequestBody MemberRequest memberRequest) throws SaveException {
        logger.info("Received request to add new member to refrigerator");
        MemberResponse result;
        try {
            result = refrigeratorService.addMember(memberRequest);
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
    public ResponseEntity<SuccessResponse> removeMember(@Valid @RequestBody RemoveMemberRequest memberRequest) {
        logger.info("Received request to remove member from refrigerator");
        try{
            refrigeratorService.removeUserFromRefrigerator(memberRequest);
            logger.info("Member removed successfully");
            return new ResponseEntity<>(new SuccessResponse("Member removed successfully", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (LastSuperuserException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create a new refrigerator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refrigerator created successfully", content = @Content(schema = @Schema(implementation = Refrigerator.class))),
            @ApiResponse(responseCode = "500", description = "Failed to create refrigerator")
    })
    @PostMapping("/new")
    public ResponseEntity<Refrigerator> newRefrigerator(@Valid @RequestBody RefrigeratorDTO refrigerator, HttpServletRequest request) throws SaveException {
        logger.info("Received request to create refrigerator for refrigerator");
        Refrigerator result = refrigeratorService.convertToEntity(refrigerator);

        String jwt = cookieService.extractTokenFromCookie(request);
        String email = jwtService.extractUsername(jwt);
        try {
            result = refrigeratorService.save(result, email);
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
    @DeleteMapping("/delete/{refrigeratorId}/{username}")
    public ResponseEntity<SuccessResponse> deleteRefrigerator(@Valid @PathVariable int refrigeratorId, @PathVariable String username) {
        try {
            refrigeratorService.forceDeleteRefrigerator(username,refrigeratorId);
            logger.info("Member removed successfully");
            return new ResponseEntity<>(new SuccessResponse("Member removed successfully", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<List<Refrigerator>> getAllByUser(@Valid @PathVariable String userName){
        logger.info("Received request for all refrigerators by user");
        try {
            List<Refrigerator> result = refrigeratorService.getAllByUser(userName);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Get all refrigerators by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of refrigerators fetched successfully", content = @Content(schema = @Schema(implementation = Refrigerator.class))),
            @ApiResponse(responseCode = "204", description = "No content")
    })
    @GetMapping("/{refrigeratorId}")
    public ResponseEntity<RefrigeratorResponse> getById(@Valid @PathVariable long refrigeratorId) {
        logger.info("Received request for refrigerator with id: {}", refrigeratorId);
        try {
            RefrigeratorResponse result = refrigeratorService.getRefrigeratorById(refrigeratorId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.warn("Refrigerator could not be found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
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
