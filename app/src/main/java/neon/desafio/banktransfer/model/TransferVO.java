package neon.desafio.banktransfer.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import neon.desafio.banktransfer.utils.MoneyUtils;

public class TransferVO implements Comparable<TransferVO>, Parcelable {

    @SerializedName("Id")
    private String id;
    @SerializedName("ClienteId")
    private String userId;
    @SerializedName("Valor")
    private double value;
    @SerializedName("Token")
    private String token;
    @SerializedName("Data")
    private String date;

    private UserVO userVO;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public double getValue() {
        return value;
    }

    public String getToken() {
        return token;
    }

    public String getDate() {
        return date;
    }

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }

    public String getFormmatedValue(){
        return MoneyUtils.formatCurrency(String.valueOf(value));
    }

    public Date getDateAsDateTime(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            Date datetime = simpleDateFormat.parse(getDate());
            return datetime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    @Override
    public int compareTo(@NonNull TransferVO other) {
        return getDateAsDateTime().compareTo(other.getDateAsDateTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeDouble(this.value);
        dest.writeString(this.token);
        dest.writeString(this.date);
        dest.writeParcelable(this.userVO, flags);
    }

    public TransferVO() {
    }

    protected TransferVO(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.value = in.readDouble();
        this.token = in.readString();
        this.date = in.readString();
        this.userVO = in.readParcelable(UserVO.class.getClassLoader());
    }

    public static final Creator<TransferVO> CREATOR = new Creator<TransferVO>() {
        @Override
        public TransferVO createFromParcel(Parcel source) {
            return new TransferVO(source);
        }

        @Override
        public TransferVO[] newArray(int size) {
            return new TransferVO[size];
        }
    };
}
