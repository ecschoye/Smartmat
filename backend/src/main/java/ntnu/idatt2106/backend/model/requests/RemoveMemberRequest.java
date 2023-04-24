package ntnu.idatt2106.backend.model.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveMemberRequest {
    @NotNull
    private String superName;
    @NotNull
    private long refrigeratorId;
    @NotNull
    private String userName;
    private boolean forceDelete;

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

    public boolean isForceDelete() {
        return forceDelete;
    }

    public void setForceDelete(boolean forceDelete) {
        this.forceDelete = forceDelete;
    }
}
