package neon.desafio.banktransfer.event;

import java.util.List;

import neon.desafio.banktransfer.model.TransferVO;

public class TransferHistorySuccess {

    private List<TransferVO> transferVOs;

    public TransferHistorySuccess(List<TransferVO> transferVOs) {
        this.transferVOs = transferVOs;
    }

    public List<TransferVO> getTransferVOs() {
        return transferVOs;
    }
}
