package neon.desafio.banktransfer.model;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import neon.desafio.banktransfer.utils.MoneyUtils;

public class TransferVO implements Comparable<TransferVO> {

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
}
