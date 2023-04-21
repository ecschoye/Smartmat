package ntnu.idatt2106.backend.service;

import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.enums.Role;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.model.requests.RefrigeratorRequest;
import ntnu.idatt2106.backend.model.dto.response.MemberResponse;
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
 *
 * TODO Generalize member data valiation
 * TODO Superuser should not be able to change to user if only superuser
 */
@Service
@RequiredArgsConstructor
public class RefrigeratorService {

    private final Role DEFAULT_USER_ROLE = Role.USER;

    @Autowired
    private final RefrigeratorRepository refrigeratorRepository;

    private final UserRepository userRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;

    private final Logger logger = LoggerFactory.getLogger(RefrigeratorService.class);

    /**
     * Adds a member to a refrigerator. Takes a member request and performs
     * necessary checks to validate that user exists, super has privileges in
     * refrigerator.
     *
     * @param request MemberRequest input
     * @return MemberReponse containing information about the affected user
     * @throws UserNotFoundException if super or user does not exist
     */
    public MemberResponse addMember(MemberRequest request) throws UserNotFoundException {
        //Get Users
        logger.debug("Getting superuser");
        User superUser = getUser(request.getSuperName());
        logger.debug("Getting new member user");
        User user = getUser(request.getUserName());

        //Get refrigerator
        Optional<Refrigerator> refrigerator = refrigeratorRepository.findById(request.getRefrigeratorId());
        if(refrigerator.isEmpty()){
            logger.warn("Member could not be added: Could not find refrigerator");
            return null;
        }

        //Check privileges
        Role privileges = getRoleById(superUser.getId(), request.getRefrigeratorId());
        if(privileges != Role.SUPERUSER){
            logger.warn("Member could not be added: User does not have super-privileges");
            return null;
        }

        RefrigeratorUser ru = new RefrigeratorUser();
        ru.setRole(DEFAULT_USER_ROLE);
        ru.setRefrigerator(refrigerator.get());
        ru.setUser(user);

        try {
            logger.info("Checks validated, saving refrigeratorUser");
            RefrigeratorUser result = refrigeratorUserRepository.save(ru);
            return new MemberResponse(result);
        } catch (Exception e) {
            logger.warn("Member could not be added: Failed to save refrigeratoruser");
            return null;
        }
    }

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
     * Gets the role a member has in a refrigerator.
     *
     * @param userId User to get role of
     * @param refrigeratorId Refrigerator associated
     * @return the Enum role
     */
    protected Role getRoleById(String userId, long refrigeratorId){
        Optional<RefrigeratorUser> refrigeratorUser = refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(userId, refrigeratorId);

        if(refrigeratorUser.isPresent()){
            return refrigeratorUser.get().getRole();
        }
        logger.warn("Superuser not present in refrigerator");
        return null;
    }

    /**
     * Validates username data and returns user by email
     *
     * @param username Email of user
     * @return User associated
     */
    protected User getUser(String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isEmpty()){
            logger.warn("Could not find user with username: {}", username);
            throw new UserNotFoundException("Could not find user with username:" + username);
        }
        return user.get();
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
        if(refrigerator.getName() == null || refrigerator.getName().length() == 0) throw new Exception("Refrigerator name is empty");
        String username = request.getUsername();
        //Check user exists
        User user;
        Refrigerator refrigeratorResult;
        try {
            user = getUser(username);
            refrigeratorResult = refrigeratorRepository.save(refrigerator);
        } catch(UserNotFoundException e){
            logger.warn("Refrigerator could not be added: User not found");
            throw new Exception("Refrigerator could not be added: User not found");
        } catch (Exception e) {
            logger.warn("Refrigerator could not be added: refrigerator could not be added");
            throw new Exception("Refrigerator could not be added: refrigerator could not be added");
        }

        //Connect user to
        RefrigeratorUser refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setRefrigerator(refrigeratorResult);
        refrigeratorUser.setUser(user);
        refrigeratorUser.setRole(Role.SUPERUSER);
        try {
            refrigeratorUserRepository.save(refrigeratorUser);
        } catch (Exception e) {
            logger.warn("Refrigerator could not be added: User could not be connected to refrigerator");
            throw new Exception("User could not be connected to refrigerator");
        }
        return refrigerator;
    }

    /**
     * Sets role of a refrigerator member
     *
     * @param request Request containing data about the super, user, role
     * @return Response containing user affected, and given role.
     */
    public MemberResponse setRole(MemberRequest request) throws UserNotFoundException {
        //Get Users
        logger.debug("Getting superuser");
        User superUser = getUser(request.getSuperName());
        logger.debug("Getting new member user");
        User user = getUser(request.getUserName());

        //Get refrigerator
        Optional<Refrigerator> refrigerator = refrigeratorRepository.findById(request.getRefrigeratorId());
        if(refrigerator.isEmpty()){
            logger.warn("Member could not be added: Could not find refrigerator");
            return null;
        }

        //Check privileges
        Role privileges = getRoleById(superUser.getId(), request.getRefrigeratorId());
        if(privileges != Role.SUPERUSER){
            logger.warn("Member could not be added: User does not have super-privileges");
            return null;
        }

        RefrigeratorUser ru = new RefrigeratorUser();
        ru.setRole(Role.USER);
        ru.setRefrigerator(refrigerator.get());
        ru.setUser(user);

        //Check if we have an instance from before
        Optional<RefrigeratorUser> existingRu = refrigeratorUserRepository.findByUserAndRefrigerator(user,refrigerator.get());
        if(existingRu.isPresent()){
            if(request.getRole() != null){
                existingRu.get().setRole(request.getRole());
                try {
                    logger.info("Checks validated, updating refrigeratorUser");
                    RefrigeratorUser result = refrigeratorUserRepository.save(existingRu.get());
                    return new MemberResponse(result);
                } catch (Exception e) {
                    logger.warn("Member could not be updated: Failed to update refrigeratoruser");
                    return null;
                }
            }
            else logger.warn("Updating role failed: request role is null");

        }
        else logger.warn("Updating role failed: user is not a member");
        return null;
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
