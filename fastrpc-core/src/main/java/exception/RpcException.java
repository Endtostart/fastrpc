package exception;


public class RpcException extends RuntimeException{

    private String message;

    public RpcException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "rpc exception :" + message;
    }
}
