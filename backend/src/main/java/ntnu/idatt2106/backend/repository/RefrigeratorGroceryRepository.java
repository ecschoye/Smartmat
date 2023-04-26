package ntnu.idatt2106.backend.repository;


import ntnu.idatt2106.backend.model.RefrigeratorGrocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefrigeratorGroceryRepository extends JpaRepository<RefrigeratorGrocery, Long> {
    @Override
    Optional<RefrigeratorGrocery> findById(Long aLong);

    List<RefrigeratorGrocery> findAllByRefrigeratorId(Long aLong);
}
