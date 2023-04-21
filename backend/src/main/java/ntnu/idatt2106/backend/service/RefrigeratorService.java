package ntnu.idatt2106.backend.service;

import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.enums.Role;
import ntnu.idatt2106.backend.model.requests.RefrigeratorRequest;
import ntnu.idatt2106.backend.repository.RefrigeratorRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorUserRepository;
import ntnu.idatt2106.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The RefrigeratorService class provides access to user data stored in the RefrigeratorRepository.
 * It provides methods for finding and manipulating user data.
 */
@Service
@RequiredArgsConstructor
public class RefrigeratorService {

    private final Role DEFAULT_USER_ROLE = Role.USER;

    @Autowired
    private final RefrigeratorRepository refrigeratorRepository;
    private final UserRepository userRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;

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
     * Saves a new refrigerator and connects the creator as a
     * superuser of the refrigerator. Performs checks on data and resets state
     * should an error occur underway.
     *
     * @param request The refrigerator request containing refrigerator to save.
     * @return The saved refrigerator.
     */
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public Refrigerator save(RefrigeratorRequest request) throws Exception {
        Refrigerator refrigerator = request.getRefrigerator();
        String username = request.getUsername();
        //TODO Check if tests can be done in constructor instead
        //Check data
        if(username == null) {
            logger.warn("Refrigerator could not be added: username is null");
            return null;
        }
        else if(username.length() == 0) {
            logger.warn("Refrigerator could not be added: username is empty");
            return null;
        }
        else if(refrigerator.getName() == null){
            logger.warn("Refrigerator could not be added: name is null");
            return null;
        }

        //Check user exists
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isEmpty()){
            logger.warn("Refrigerator could not be added: user not found");
            return null;
        }

        //Create Refrigerator
        Refrigerator refrigeratorResult = null;
        try {
            refrigeratorResult = refrigeratorRepository.save(refrigerator);
        } catch (Exception e) {
            logger.warn("Refrigerator could not be added: refrigerator could not be added");
            throw new Exception("Refrigerator could not be added");
        }

        //Connect user to
        RefrigeratorUser refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setRefrigerator(refrigeratorResult);
        refrigeratorUser.setUser(user.get());
        refrigeratorUser.setRole(Role.SUPERUSER);
        RefrigeratorUser refrigeratorUserResult = null;
        try {
            refrigeratorUserResult =  refrigeratorUserRepository.save(refrigeratorUser);
        } catch (Exception e) {
            logger.warn("Refrigerator could not be added: User could not be connected to refrigerator");
            throw new Exception("User could not be connected to refrigerator");
        }
        return refrigerator;
    }



    /**
     * Deletes a refrigerator by ID.
     *
     * @param id The ID of the refrigerator to delete.
     * @return true if the refrigerator was successfully deleted, false otherwise
     */
    public boolean deleteById(long id) {
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
