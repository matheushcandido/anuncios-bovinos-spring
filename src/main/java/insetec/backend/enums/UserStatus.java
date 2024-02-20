package insetec.backend.enums;

public enum UserStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    BLOCKED("blocked");

    private String userStatus;

    UserStatus(String userStatus){
        this.userStatus = userStatus;
    }

    public String getUserStatus(){
        return userStatus;
    }
}

