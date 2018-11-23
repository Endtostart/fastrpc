package fastrpc.context.exception;

public class WeaveException extends RuntimeException{
    public WeaveException() {
        super();
    }

    public WeaveException(String message) {
        super(message);
    }
}
