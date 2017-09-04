package neon.desafio.banktransfer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class TransferTotalChart implements Comparable<TransferTotalChart>, Parcelable {

    private UserVO userVO;
    private double totalTransfered;

    public TransferTotalChart(UserVO userVO) {
        this.userVO = userVO;
        this.totalTransfered = 0.0;
    }

    public void addValue(double value) {
        totalTransfered += value;
    }

    public UserVO getUserVO() {
        return userVO;
    }

    public double getTotalTransfered() {
        return totalTransfered;
    }

    @Override
    public int compareTo(@NonNull TransferTotalChart other) {
        return Double.compare(other.getTotalTransfered(), getTotalTransfered());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userVO, flags);
        dest.writeDouble(this.totalTransfered);
    }

    protected TransferTotalChart(Parcel in) {
        this.userVO = in.readParcelable(UserVO.class.getClassLoader());
        this.totalTransfered = in.readDouble();
    }

    public static final Creator<TransferTotalChart> CREATOR = new Creator<TransferTotalChart>() {
        @Override
        public TransferTotalChart createFromParcel(Parcel source) {
            return new TransferTotalChart(source);
        }

        @Override
        public TransferTotalChart[] newArray(int size) {
            return new TransferTotalChart[size];
        }
    };
}
