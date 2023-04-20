package ntnu.idatt2106.backend.repository;


import ntnu.idatt2106.backend.model.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroceryRepository extends JpaRepository<Grocery, Long>{
    @Override
    Optional<Grocery> findById(Long aLong);
}
