package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    Optional<ShoppingList> findById(Long id);

    @Query("SELECT s FROM ShoppingList s WHERE s.refrigerator.id = :refrigeratorId")
    Optional<ShoppingList> findByRefrigeratorId(@Param(value = "refrigeratorId") Long id);

    @Query(value = "SELECT s.refrigerator FROM ShoppingList s WHERE s.id = :id")
    Refrigerator findRefrigeratorById(Long id);

    void removeByRefrigerator_Id(long refrigeratorId);
}
