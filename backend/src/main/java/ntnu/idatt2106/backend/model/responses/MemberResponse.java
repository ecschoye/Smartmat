package ntnu.idatt2106.backend.model.responses;

import ntnu.idatt2106.backend.model.enums.Role;

public class MemberResponse {
    private long refrigeratorId;
    private String username;
    private Role role;

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
