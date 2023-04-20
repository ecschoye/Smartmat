package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroceryRepository extends JpaRepository<Grocery, Integer> {

    @Query(value = "SELECT g FROM Grocery g")
    List<Grocery> getAllGrocery();
}
