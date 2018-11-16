package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class NettyClient {

    private String ip;
    private int port;
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private boolean started;
    private boolean stopped;

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

    public void send(String message) {
        System.out.println("send message to provider:" + message);
    }


}
