package ntnu.idatt2106.backend.repository;

import ntnu.idatt2106.backend.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>{
    @Override
    Optional<SubCategory> findById(Long aLong);

    Optional<SubCategory> findSubCategoryByName(String name);
}

