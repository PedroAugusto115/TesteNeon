package neon.desafio.banktransfer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserVO implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userName);
        dest.writeString(this.userEmail);
        dest.writeString(this.userPhone);
    }

    protected UserVO(Parcel in) {
        this.id = in.readString();
        this.userName = in.readString();
        this.userEmail = in.readString();
        this.userPhone = in.readString();
    }

    public static final Creator<UserVO> CREATOR = new Creator<UserVO>() {
        @Override
        public UserVO createFromParcel(Parcel source) {
            return new UserVO(source);
        }

        @Override
        public UserVO[] newArray(int size) {
            return new UserVO[size];
        }
    };
}