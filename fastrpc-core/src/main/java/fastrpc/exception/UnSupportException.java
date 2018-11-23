package fastrpc.exception;

public class UnSupportException extends RuntimeException{
    public UnSupportException(String message) {
        super("Rpc unSupportException:" + message);
    }
}
