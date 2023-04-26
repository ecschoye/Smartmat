package ntnu.idatt2106.backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.exceptions.LastSuperuserException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.dto.refrigerator.RefrigeratorDTO;
import ntnu.idatt2106.backend.model.dto.response.RefrigeratorResponse;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.model.dto.response.MemberResponse;
import ntnu.idatt2106.backend.model.requests.RemoveMemberRequest;
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

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
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

    private final FridgeRole DEFAULT_USER_Fridge_ROLE = FridgeRole.USER;

    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;

    private final UserRepository userRepository;

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
        FridgeRole privileges = getFridgeRole(superUser.getId(), request.getRefrigeratorId());
        if(privileges != FridgeRole.SUPERUSER){
            logger.warn("Member could not be added: User does not have super-privileges");
            return null;
        }

        RefrigeratorUser ru = new RefrigeratorUser();
        ru.setFridgeRole(DEFAULT_USER_Fridge_ROLE);
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
     * Retrieves all refrigerators where a given user is a member
     *
     * @return A list containing all refrigerators.
     */
    public List<Refrigerator> getAllByUser(String username) throws UserNotFoundException{
        User user = getUser(username);
        List<Refrigerator> refrigerators = new ArrayList<>();
        refrigeratorUserRepository.findByUser(user).forEach(ru -> {refrigerators.add(ru.getRefrigerator());});
        return refrigerators;
    }

    public Refrigerator convertToEntity(RefrigeratorDTO refrigeratorDTO){
        if (refrigeratorDTO == null) return null;


        Refrigerator refrigerator = Refrigerator.builder()
                .id(refrigeratorDTO.getId())
                .name(refrigeratorDTO.getName())
                .address(refrigeratorDTO.getAddress())
                .build();

        return refrigerator;
    }

    /**
     * Gets a refrigerator by id and all data we
     * want the user to know about the refrigerator.
     *
     * @param id Id of refrigerator to get
     * @return RefrigeratorResponse
     */
    public RefrigeratorResponse getRefrigeratorById(long id) throws EntityNotFoundException {
        if(refrigeratorRepository.existsById(id)){
            Refrigerator refrigerator = refrigeratorRepository.findById(id)
                    .orElse(null);
            if(refrigerator == null) throw new EntityNotFoundException("Could not find refrigerator");
            List<MemberResponse> users = new ArrayList<>();
            refrigeratorUserRepository.findByRefrigeratorId(id)
                    .forEach((ru -> users.add(new MemberResponse(ru))));

            return new RefrigeratorResponse(refrigerator, users);
        }
        else throw new EntityNotFoundException("Refrigerator does not exist");
    }

    /**
     * Gets the role a member has in a refrigerator.
     *
     * @param userId User to get role of
     * @param refrigeratorId Refrigerator associated
     * @return the Enum role
     */
    protected FridgeRole getFridgeRole(String userId, long refrigeratorId){
        Optional<RefrigeratorUser> refrigeratorUser = refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(userId, refrigeratorId);

        if(refrigeratorUser.isPresent()){
            return refrigeratorUser.get().getFridgeRole();
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
     * Removes user from refrigerator. Does check to always
     * keep at least one superuser in the refrigerator.
     *
     * @param request request object containing necessary data.
     * @throws AccessDeniedException If user tries to remove other users
     * @throws LastSuperuserException If superuser tries to remove himself as last superuser
     * @throws EntityNotFoundException If request data is invalid.
     */
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeUserFromRefrigerator(RemoveMemberRequest request) throws Exception {
        if(request.isForceDelete()) {
            forceDeleteRefrigerator(request.getSuperName(),request.getRefrigeratorId());
        }

        Refrigerator refrigerator = refrigeratorRepository.findById(request.getRefrigeratorId())
                .orElseThrow(() -> new EntityNotFoundException("Refrigerator not found"));
        User user = userRepository.findByEmail(request.getUserName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        User superUser = userRepository.findByEmail(request.getSuperName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        RefrigeratorUser userRole = refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.getId(), refrigerator.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not a member"));
        RefrigeratorUser superuserRole = refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superUser.getId(), refrigerator.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not a member"));

        logger.info("Performing member removal");
        if(userRole.getFridgeRole() == FridgeRole.USER && user.getId().equals(superUser.getId())){
            logger.debug("User deleting himself");
            refrigeratorUserRepository.delete(userRole);
        }
        else if(userRole.getFridgeRole() == FridgeRole.SUPERUSER){
            List<RefrigeratorUser> superUsers = refrigeratorUserRepository.findByRefrigeratorIdAndFridgeRole(refrigerator.getId(), FridgeRole.SUPERUSER);
            if(superUsers.size() <= 1) {
                throw new LastSuperuserException("Last superuser in refrigerator");
            }
            else {
                refrigeratorUserRepository.delete(userRole);
            }
        }
        else {
            if(superuserRole.getFridgeRole() != FridgeRole.SUPERUSER) {
                logger.warn("User attempted to remove another user(!)");
                throw new AccessDeniedException("User cannot remove other users");
            }
            refrigeratorUserRepository.delete(userRole);
        }
        refrigeratorRepository.save(refrigerator);
    }



    /**
     * Saves a new refrigerator and connects the creator as a
     * superuser of the refrigerator. Performs checks on data and resets state
     * should an error occur underway.
     *
     * @param refrigerator request containing refrigerator to save.
     * @return The saved refrigerator.
     */
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public Refrigerator save(Refrigerator refrigerator, String email) throws Exception {

        if(refrigerator.getName() == null || refrigerator.getName().length() == 0) throw new Exception("Refrigerator name is empty");
        //Check user exists
        User user;
        Refrigerator refrigeratorResult;
        try {
            user = getUser(email);
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
        refrigeratorUser.setFridgeRole(FridgeRole.SUPERUSER);
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
    public MemberResponse setFridgeRole(MemberRequest request) throws UserNotFoundException {
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
        FridgeRole privileges = getFridgeRole(superUser.getId(), request.getRefrigeratorId());
        if(privileges != FridgeRole.SUPERUSER){
            logger.warn("Member could not be added: User does not have super-privileges");
            return null;
        }

        RefrigeratorUser ru = new RefrigeratorUser();
        ru.setFridgeRole(FridgeRole.USER);
        ru.setRefrigerator(refrigerator.get());
        ru.setUser(user);

        //Check if we have an instance from before
        Optional<RefrigeratorUser> existingRu = refrigeratorUserRepository.findByUserAndRefrigerator(user,refrigerator.get());
        if(existingRu.isPresent()){
            if(request.getRole() != null){
                existingRu.get().setFridgeRole(request.getRole());
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
     * Forcefully deletes a refrigerator and all its members. Gets
     * the user that requested the delete and checks permission. Then
     * deletes any members and the refrigerator itself.
     *
     * TODO: DELETE INVENTORY ++
     * @param refrigeratorId The ID of the refrigerator to delete.
     */
    @Transactional(propagation =  Propagation.REQUIRED, rollbackFor = Exception.class)
    public void forceDeleteRefrigerator(String supername, long refrigeratorId) throws Exception {
        logger.info("Force delete was requested");
        User superUser = userRepository.findByEmail(supername)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        RefrigeratorUser superuserRole = refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superUser.getId(), refrigeratorId)
                .orElseThrow(() -> new EntityNotFoundException("User not a member"));
        if(superuserRole.getFridgeRole() != FridgeRole.SUPERUSER){
            logger.error("Failed to delete refrigerator: User not superuser");
            throw new AccessDeniedException("Failed to delete refrigerator: User not superuser");
        }
        if (refrigeratorRepository.existsById(refrigeratorId)) {
            long members = refrigeratorUserRepository.count();
            if(members > 0){
                try {
                    refrigeratorUserRepository.removeByRefrigeratorId(refrigeratorId);
                } catch (Exception e) {
                    logger.error("Failed to delete refrigerator: failed to remove refrigerator members, {}",e.getMessage());
                    throw e;
                }
            }
            try {
                refrigeratorRepository.deleteById(refrigeratorId);
                return;
            } catch (EmptyResultDataAccessException e) {
                logger.error("Failed to delete refrigerator: Id {}: {},", refrigeratorId, e.getMessage());
            }
        }
        else{
            logger.error("Failed to delete refrigerator: Refrigerator with id {} does not exist", refrigeratorId);
            throw new EntityNotFoundException("Failed to delete refrigerator: Refrigerator does not exist");
        }
        throw new Exception("Failed to delete refrigerator");
    }
}
