package fastrpc.exception;

import fastrpc.netty.VirtualServer;

public class RpcCallbackException extends RpcException{

    byte[] data;
    public RpcCallbackException(String message,byte[] data) {
        super(message);
        this.data = data;
    }

    public void doCallBack(VirtualServer virtualServer) {
        virtualServer.response(this.data);
    }


}
