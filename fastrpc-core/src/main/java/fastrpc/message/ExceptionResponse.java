package fastrpc.message;

import fastrpc.exception.RpcException;

public class ExceptionResponse extends Response<RpcException>{
    public ExceptionResponse(String message) {
        super(new RpcException(message));
    }

    @Override
    public boolean hasException() {
        return true;
    }
}
