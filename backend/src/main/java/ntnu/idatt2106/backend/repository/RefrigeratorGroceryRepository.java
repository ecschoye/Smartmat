package ntnu.idatt2106.backend.repository;


import ntnu.idatt2106.backend.model.Grocery;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorGrocery;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefrigeratorGroceryRepository extends JpaRepository<RefrigeratorGrocery, Long> {
    @Override
    Optional<RefrigeratorGrocery> findById(Long aLong);

    List<RefrigeratorGrocery> findAllByRefrigeratorId(Long aLong);

    void removeByRefrigeratorId(long refrigeratorId);

    @Query("SELECT CASE WHEN COUNT(rg) > 0 THEN true ELSE false END FROM RefrigeratorGrocery rg WHERE rg.refrigerator = :refrigerator AND rg.grocery = :grocery")
    boolean existsByRefrigeratorAndGrocery(@Param("refrigerator") Refrigerator refrigerator, @Param("grocery") Grocery grocery);
}
