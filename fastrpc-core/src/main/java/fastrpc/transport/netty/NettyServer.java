package fastrpc.transport.netty;

import fastrpc.transport.Server;

public class NettyServer implements Server,Runnable {
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    @Override
    public void service() {

    }

    @Override
    public void run() {

    }
}
