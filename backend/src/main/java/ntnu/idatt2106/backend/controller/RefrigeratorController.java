package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.LastSuperuserException;
import ntnu.idatt2106.backend.exceptions.SaveException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.dto.response.SuccessResponse;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.model.requests.RefrigeratorRequest;
import ntnu.idatt2106.backend.model.dto.response.MemberResponse;
import ntnu.idatt2106.backend.model.requests.RemoveMemberRequest;
import ntnu.idatt2106.backend.service.RefrigeratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/refrigerator")
@RequiredArgsConstructor
@Tag(name = "Refrigerator Controller", description = "Controller to handle the refrigerator")
public class RefrigeratorController {

    private final RefrigeratorService refrigeratorService;

    Logger logger = LoggerFactory.getLogger(RefrigeratorController.class);

    @PostMapping("/members/edit-role")
    public ResponseEntity<MemberResponse> editRole(@Valid @RequestBody MemberRequest memberRequest) throws SaveException {
        logger.info("Received request to edit member role in refrigerator");
        MemberResponse result;
        try {
            result = refrigeratorService.setRole(memberRequest);
            if (result == null) throw new Exception();
        } catch (Exception e) {
            throw new SaveException("Failed to edit role");
        }
        logger.info("Returning response");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

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
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Refrigerator> newRefrigerator(@Valid @RequestBody RefrigeratorRequest refrigerator) throws SaveException {
        logger.info("Received request to create refrigerator for refrigerator");
        Refrigerator result;
        try {
            result = refrigeratorService.save(refrigerator);
            if (result == null) throw new Exception();
        } catch (Exception e) {
            throw new SaveException("Failed to create refrigerator");
        }
        logger.info("Returning refrigerator with id {}", result.getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{refrigeratorId}")
    public ResponseEntity<Void> deleteRefrigerator(@Valid @PathVariable int refrigeratorId) {
        boolean deleted = refrigeratorService.deleteById(refrigeratorId);
        if (deleted) {
            logger.info("Deleted refrigerator with id {}", refrigeratorId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.info("Failed to delete refrigerator with id {}", refrigeratorId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Refrigerator>> getAll(){
        logger.info("Received request for all refrigerators");
        return new ResponseEntity<>(refrigeratorService.getAllRefrigerators(), HttpStatus.OK);
    }

    @GetMapping("/{refrigeratorId}")
    public ResponseEntity<Refrigerator> getById(@Valid @PathVariable int refrigeratorId) {
        logger.info("Received request for refrigerator with id: {}", refrigeratorId);
        Optional<Refrigerator> result = refrigeratorService.findById(refrigeratorId);
        if(result.isEmpty()){
            logger.warn("Refrigerator with id: {} could not be found", refrigeratorId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            logger.info("Returning refrigerator");
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
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
