package ntnu.idatt2106.backend.model.dto.response;

import ntnu.idatt2106.backend.model.Refrigerator;

import java.util.List;

public class RefrigeratorResponse {
    private long id;
    private String name;
    private String address;
    private List<MemberResponse> members;

    public RefrigeratorResponse() {}

    public RefrigeratorResponse(Refrigerator refrigerator, List<MemberResponse> members) {
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

    public List<MemberResponse> getMembers() {
        return members;
    }

    public void setMembers(List<MemberResponse> members) {
        this.members = members;
    }
}
