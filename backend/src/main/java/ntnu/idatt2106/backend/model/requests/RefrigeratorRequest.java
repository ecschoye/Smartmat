package ntnu.idatt2106.backend.model.requests;

import ntnu.idatt2106.backend.model.Refrigerator;

public class RefrigeratorRequest {
    private String username;
    private Refrigerator refrigerator;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Refrigerator getRefrigerator() {
        return refrigerator;
    }

    public void setRefrigerator(Refrigerator refrigerator) {
        this.refrigerator = refrigerator;
    }
}
