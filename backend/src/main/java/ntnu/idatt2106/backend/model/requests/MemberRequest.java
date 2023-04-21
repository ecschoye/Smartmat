package ntnu.idatt2106.backend.model.requests;

import ntnu.idatt2106.backend.model.enums.Role;

public class MemberRequest {
    private String superName;
    private long refrigeratorId;
    private String userName;
    private Role role;

    public String getSuperName() {
        return superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public long getRefrigeratorId() {
        return refrigeratorId;
    }

    public void setRefrigeratorId(long refrigeratorId) {
        this.refrigeratorId = refrigeratorId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
