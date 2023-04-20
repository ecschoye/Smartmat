package ntnu.idatt2106.backend.service;

import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.repository.RefrigeratorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The RefrigeratorService class provides access to user data stored in the RefrigeratorRepository.
 * It provides methods for finding and manipulating user data.
 */
@Service
@RequiredArgsConstructor
public class RefrigeratorService {

    @Autowired
    private final RefrigeratorRepository refrigeratorRepository;

    private Logger logger = LoggerFactory.getLogger(RefrigeratorService.class);

    /**
     * Finds a refrigerator by ID.
     *
     * @param id The ID of the refrigerator to find.
     * @return An Optional containing the refrigerator with the specified ID, or an empty Optional if no such refrigerator exists.
     */
    public Optional<Refrigerator> findById(long id) {
        if(id < 0) return Optional.empty();
        return refrigeratorRepository.findById(id);
    }

    /**
     * Retrieves all refrigerators.
     *
     * @return A list containing all refrigerators.
     */
    public List<Refrigerator> getAllRefrigerators() {
        return refrigeratorRepository.findAll();
    }

    /**
     * Saves a new refrigerator.
     *
     * @param refrigerator The refrigerator to save.
     * @return The saved refrigerator.
     */
    public Refrigerator save(Refrigerator refrigerator) {
        if(refrigerator.getName() == null){
            logger.warn("Refrigerator could not be added: name is null");
            return null;
        }
        else if(refrigerator.getAddress() == null){
            logger.warn("Refrigerator could not be added: address is null");
            return null;
        }
        return refrigeratorRepository.save(refrigerator);
    }


    /**
     * Deletes a refrigerator by ID.
     *
     * @param id The ID of the refrigerator to delete.
     * @return true if the refrigerator was successfully deleted, false otherwise
     */
    public boolean delete(long id) {
        if (refrigeratorRepository.existsById(id)) {
            try {
                refrigeratorRepository.deleteById(id);
                return true;
            } catch (EmptyResultDataAccessException e) {
                logger.error("Failed to delete refrigerator with id {}: {}", id, e.getMessage());
            }
        }
        logger.error("Refrigerator with id {} does not exist", id);
        return false;
    }
}
