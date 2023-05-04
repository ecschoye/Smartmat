package ntnu.idatt2106.backend.repository;


import ntnu.idatt2106.backend.model.GroceryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroceryHistoryRepository extends JpaRepository<GroceryHistory, Long> {
    @Override
    Optional<GroceryHistory> findById(Long l);

    List<GroceryHistory> findAllByDateConsumedAfter(LocalDate date);
    List<GroceryHistory> findByDateConsumedBetweenAndRefrigeratorId(LocalDate startDate, LocalDate endDate, Long refrigeratorId);
    void deleteByDateConsumedBefore(LocalDate date);
}
