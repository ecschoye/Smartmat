package ntnu.idatt2106.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ntnu.idatt2106.backend.model.Unit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDTO {
    private Long id;
    private String name;

    public UnitDTO(Unit unit){
        this.id = unit.getId();
        this.name = unit.getName();
    }
}
