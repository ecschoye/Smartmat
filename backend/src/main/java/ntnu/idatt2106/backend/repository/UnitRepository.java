package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Override
    Optional<Unit> findById(Long aLong);
}