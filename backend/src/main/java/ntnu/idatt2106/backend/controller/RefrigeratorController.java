package ntnu.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.SaveException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.requests.RefrigeratorRequest;
import ntnu.idatt2106.backend.service.RefrigeratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/refrigerator")
@RequiredArgsConstructor
@Tag(name = "Refrigerator Controller", description = "Controller to handle the refrigerator")
public class RefrigeratorController {

    private final RefrigeratorService refrigeratorService;

    Logger logger = LoggerFactory.getLogger(RefrigeratorController.class);

    @PostMapping("/create")
    public ResponseEntity<Refrigerator> createRefrigerator(@RequestBody RefrigeratorRequest refrigerator) throws SaveException {
        logger.info("Received request to create refrigerator for refrigerator");
        Refrigerator result = null;
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
    public ResponseEntity<Void> deleteRefrigerator(@PathVariable int refrigeratorId) {
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
    public ResponseEntity<Refrigerator> getById(@PathVariable int refrigeratorId) {
        logger.info("Received request for refrigerator with id: {}", refrigeratorId);
        Optional<Refrigerator> result = refrigeratorService.findById(refrigeratorId);
        if(result.isEmpty()){
            logger.warn("Refrigerator with id: {} could not be found", refrigeratorId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            logger.info("Returning refrigerator");
            return new ResponseEntity<Refrigerator>(result.get(), HttpStatus.OK);
        }
    }
}
