package ntnu.idatt2106.backend.repository;


import ntnu.idatt2106.backend.model.GroceryNotification;
import ntnu.idatt2106.backend.model.RefrigeratorGrocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroceryNotificationRepository extends JpaRepository<GroceryNotification, Long> {
    @Override
    Optional<GroceryNotification> findById(Long aLong);

    List<GroceryNotification> findAllByUserId(String userId);

    List<GroceryNotification> findAllByGroceryEntity(RefrigeratorGrocery groceryEntity);

}
