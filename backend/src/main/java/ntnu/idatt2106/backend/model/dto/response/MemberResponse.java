package ntnu.idatt2106.backend.model.dto.response;

import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.enums.FridgeRole;

/**
 * Model class used to return public information about a member in a refrigerator
 */
public class MemberResponse {
    private long refrigeratorId;
    private String name;
    private String username;
    private FridgeRole fridgeRole;

    public MemberResponse() {}

    public MemberResponse(RefrigeratorUser ru) {
        this.refrigeratorId = ru.getRefrigerator().getId();
        this.name = ru.getUser().getName();
        this.username = ru.getUser().getUsername();
        this.fridgeRole = ru.getFridgeRole();
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

    public FridgeRole getRole() {
        return fridgeRole;
    }

    public void setRole(FridgeRole fridgeRole) {
        this.fridgeRole = fridgeRole;
    }
}
