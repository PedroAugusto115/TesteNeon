package neon.desafio.banktransfer.event;

public class ErrorEvent {

    private Type errorType;

    public ErrorEvent(Type errorType) {
        this.errorType = errorType;
    }

    public Type getErrorType() {
        return errorType;
    }

    public enum Type {
        NOT_FOUND, BAD_REQUEST, GENERIC_ERROR, UNAUTHORIZED, SERVER_ERROR, NETWORK_ERROR
    }
}
