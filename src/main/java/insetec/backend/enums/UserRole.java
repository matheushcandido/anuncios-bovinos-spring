package insetec.backend.enums;

public enum UserRole {

    ADMIN("admin"),
    USER("user"),
    SELLER("seller");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
