package fastrpc.netty;

import fastrpc.context.ApplicationContext;
import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.Weave;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import fastrpc.netty.base.SocketBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Bean
public class NettyClient{

    Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String ip;
    private int port;
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private boolean started;
    private boolean stopped;

    @Weave
    private VirtualServer virtualServer;
    @Weave
    private SocketBuffer socketBuffer;

    private ExecutorService executors = Executors.newCachedThreadPool();
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

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

    public byte[] send(byte[] message) {
        logger.info("send fastrpc.message to provider:" + message);

        FutureTask<byte[]> future = new FutureTask(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                try{
                    lock.lock();
                    socketBuffer.setServerStack(message);
                    logger.info("\n client = = =>>" + Thread.currentThread().getName() + " : set socketBuffer ->: " + message);

                    socketBuffer.addClientListener(()->{
                        lock.lock();
                        logger.info("\n client = = =>>" + Thread.currentThread().getName() + " signal");
                        condition.signal();
                        lock.unlock();
                    });

                    while (!socketBuffer.hasClientMsg()) {
                        logger.info("\n client = = =>>" + Thread.currentThread().getName() + " begain await");
                        condition.await();
                        logger.info("\n client = = =>>" + Thread.currentThread().getName() + " end await");
                    }
                    logger.info("\n client = = =>>" + Thread.currentThread().getName() + " out loop -->\n result msg:" + socketBuffer.getClientStack());
                    return socketBuffer.getClientStack();
                }finally {
                    lock.unlock();
                }

            }
        });

        executors.submit(future);

        byte[] res = null;
        try {
            res = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return res;
    }

    public VirtualServer bindVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
        return virtualServer;
    }

    public void unBindVirtualServer() {
        this.virtualServer = null;
    }

}
