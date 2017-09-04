package neon.desafio.banktransfer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SendMoneyRequestVO implements Parcelable {

    @SerializedName("ClienteId")
    private final String id;
    @SerializedName("token")
    private final String token;
    @SerializedName("valor")
    private final double value;

    public SendMoneyRequestVO(String id, String token, double value) {
        this.id = id;
        this.token = token;
        this.value = value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.token);
        dest.writeDouble(this.value);
    }

    protected SendMoneyRequestVO(Parcel in) {
        this.id = in.readString();
        this.token = in.readString();
        this.value = in.readDouble();
    }

    public static final Creator<SendMoneyRequestVO> CREATOR = new Creator<SendMoneyRequestVO>() {
        @Override
        public SendMoneyRequestVO createFromParcel(Parcel source) {
            return new SendMoneyRequestVO(source);
        }

        @Override
        public SendMoneyRequestVO[] newArray(int size) {
            return new SendMoneyRequestVO[size];
        }
    };
}
