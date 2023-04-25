package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorGrocery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefrigeratorGroceryRepository extends JpaRepository<RefrigeratorGrocery,Long> {

    List<RefrigeratorGrocery> findByRefrigerator(Refrigerator refrigerator);
}
