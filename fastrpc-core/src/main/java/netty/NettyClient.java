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

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class NettyClient{

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

    public String send(String message) {
        logger.info("send message to provider:" + message);

        FutureTask<String> future = new FutureTask(new Callable<String>() {
            @Override
            public String call() throws Exception {
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

        String res = null;
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

    public VirtualServer unBindVirtualServer() {
        this.virtualServer = noneVirtualServer;
        return noneVirtualServer;
    }


}
