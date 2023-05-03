package ntnu.idatt2106.backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.Unit;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;


    public RefrigeratorGrocery convertGrocery(RefrigeratorGrocery refrigeratorGrocery, long unitId){
        Optional<Unit> newUnit = unitRepository.findById(unitId);
        if(newUnit.isEmpty()){
            throw new EntityNotFoundException("Could not find unit with unitId" + unitId);
        }
        int newQuantity = (refrigeratorGrocery.getQuantity()*refrigeratorGrocery.getUnit().getWeight())/newUnit.get().getWeight();
        refrigeratorGrocery.setQuantity(newQuantity);
        refrigeratorGrocery.setUnit(newUnit.get());
        return refrigeratorGrocery;
    }
}
