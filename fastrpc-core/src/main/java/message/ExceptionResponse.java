package message;

import exception.RpcException;

public class ExceptionResponse extends Response<RpcException>{
    public ExceptionResponse(String message) {
        super(new RpcException(message));
    }
}
