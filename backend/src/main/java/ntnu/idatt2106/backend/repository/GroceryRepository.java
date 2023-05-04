package ntnu.idatt2106.backend.repository;


import ntnu.idatt2106.backend.model.grocery.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroceryRepository extends JpaRepository<Grocery, Long>{
    @Override
    Optional<Grocery> findById(Long aLong);

    Optional<Grocery> findByName(String name);

    @Modifying
    @Query(value = "ALTER TABLE my_entity ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrement();
}
