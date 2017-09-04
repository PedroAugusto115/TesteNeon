package neon.desafio.banktransfer.event;

public class TransferMoneySuccessEvent {

    private boolean isSuccess;

    public TransferMoneySuccessEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
