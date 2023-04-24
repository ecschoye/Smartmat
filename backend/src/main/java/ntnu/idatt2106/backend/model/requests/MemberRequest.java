package ntnu.idatt2106.backend.model.requests;

import jakarta.validation.constraints.NotNull;
import ntnu.idatt2106.backend.model.enums.Role;

/**
 * Model used to receive a request for adding or editing a member
 * of a refrigerator.
 */
public class MemberRequest {
    @NotNull
    private String superName;
    @NotNull
    private long refrigeratorId;
    @NotNull
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
