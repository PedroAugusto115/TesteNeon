package neon.desafio.banktransfer.model;

import android.support.annotation.NonNull;

public class TransferTotalChart implements Comparable<TransferTotalChart> {

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
}
