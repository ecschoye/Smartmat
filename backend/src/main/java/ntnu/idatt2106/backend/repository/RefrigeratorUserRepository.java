package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefrigeratorUserRepository extends JpaRepository<RefrigeratorUser, Long> {
    Optional<RefrigeratorUser> findByUser_IdAndRefrigerator_Id(String userId, long refrigeratorId);

    Optional<RefrigeratorUser> findByUserAndRefrigerator(User user, Refrigerator refrigerator);

    List<RefrigeratorUser> findByRefrigeratorIdAndRole(long refrigeratorId, Role role);

    List<RefrigeratorUser> findByRefrigeratorId(long refrigeratorId);

    List<RefrigeratorUser> findByUser(User user);

    void removeByRefrigeratorId(long refrigeratorId);
}
