package netty;

import context.ApplicationContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.base.SocketBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyClient extends Thread{

    Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String ip;
    private int port;
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private boolean started;
    private boolean stopped;


    private VirtualServer virtualServer = (VirtualServer) ApplicationContext.get(VirtualServer.class);
    private VirtualServer noneVirtualServer = new VirtualServer();
    private SocketBuffer socketBuffer = (SocketBuffer) ApplicationContext.get(SocketBuffer.class);



    public NettyClient() {

    }

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.init();
    }

    private void init() {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(1);
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>(){

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
            }
        });
    }

    @Override
    public void run() {

    }

    public String send(String message) {
        logger.info("send message to provider:" + message);
        socketBuffer.setServerStack(message);
        return "ok";
    }

    public VirtualServer bindVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
        return virtualServer;
    }

    public VirtualServer unBindVirtualServer() {
        this.virtualServer = noneVirtualServer;
        return noneVirtualServer;
    }


}
