package neon.desafio.banktransfer.model;

public class UserVO {

    private String id;
    private String userName;
    private String userEmail;
    private String userPhone;

    public UserVO(){}

    public UserVO(String id, String userName, String userEmail, String userPhone) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserImageUrl() {
        return String.format("https://api.adorable.io/avatars/150/%s.png", userName);
    }
}