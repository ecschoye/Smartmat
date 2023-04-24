package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.dto.MemberDTO;

import java.util.List;

/**
 * Model for creating refrigerator response to frontend.
 * Provides all information about a refrigerator and its users.
 */
public class RefrigeratorDTO {
    private long id;
    private String name;
    private String address;
    private List<MemberDTO> members;

    public RefrigeratorDTO() {}

    public RefrigeratorDTO(Refrigerator refrigerator, List<MemberDTO> members) {
        this.id = refrigerator.getId();
        this.name = refrigerator.getName();
        this.address = refrigerator.getAddress();
        this.members = members;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<MemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDTO> members) {
        this.members = members;
    }
}
