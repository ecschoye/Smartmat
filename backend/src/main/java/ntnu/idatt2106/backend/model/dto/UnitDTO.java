package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.Unit;

public class UnitDTO {
    private Long id;
    private String name;
    public UnitDTO(Unit unit){
        this.id = unit.getId();
        this.name = unit.getName();
    }
}
