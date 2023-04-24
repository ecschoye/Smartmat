package ntnu.idatt2106.backend.model.dto;

import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.enums.Role;

/**
 * Model class used to return public information about a member in a refrigerator
 */
public class MemberDTO {
    private long refrigeratorId;
    private String name;
    private String username;
    private Role role;

    public MemberDTO() {}

    public MemberDTO(RefrigeratorUser ru) {
        this.refrigeratorId = ru.getRefrigerator().getId();
        this.name = ru.getUser().getName();
        this.username = ru.getUser().getUsername();
        this.role = ru.getRole();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRefrigeratorId() {
        return refrigeratorId;
    }

    public void setRefrigeratorId(long refrigeratorId) {
        this.refrigeratorId = refrigeratorId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
