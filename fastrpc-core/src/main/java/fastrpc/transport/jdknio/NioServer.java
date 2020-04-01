package fastrpc.transport.jdknio;

import fastrpc.context.support.ApplicationUtils;
import fastrpc.netty.base.SocketBuffer;
import fastrpc.rpc.ProviderService;
import fastrpc.transport.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer extends Thread implements Server {
    private ProviderService providerService;
    private SocketBuffer socketBuffer;
    Logger logger = LoggerFactory.getLogger(NioServer.class);

    private final int port;
    private ServerSocketChannel ssc;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(2048);

    public NioServer(int port) {
        this.port = port;
        this.providerService = ApplicationUtils.getBeanContent().getBean(ProviderService.class);
        this.socketBuffer = ApplicationUtils.getBeanContent().getBean(SocketBuffer.class);
    }

    @Override
    public void service() throws IOException {
        InetSocketAddress inet = new InetSocketAddress("localhost",port);
        ssc = ServerSocketChannel.open();
        ssc.socket().bind(inet);

        selector = Selector.open();
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("\n= = = 服务启动 = = = \n监听地址：" + inet.getHostName() + ": " + port);

        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iKeys = keys.iterator();
            while (iKeys.hasNext()) {
                SelectionKey key = iKeys.next();
                iKeys.remove();
                SocketChannel sc;
                if (key.isAcceptable()) {
                    ServerSocketChannel scc = (ServerSocketChannel) key.channel();
                    sc = scc.accept();
                    logger.info("客户端： "+sc.getRemoteAddress()+" =》 连接成功");
                    sc.configureBlocking(false);
                    buffer.clear();
                    buffer.get("hello from server".getBytes());
                    sc.write(buffer);

                    sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                } else if (key.isReadable()) {
                    sc = (SocketChannel) key.channel();
                    buffer.clear();
                    sc.read(buffer);
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes, 0, buffer.remaining());
                    providerService.doProcess(bytes);

                    byte[] result = socketBuffer.getClientStack();
                    buffer.clear();
                    buffer.put(result);
                    buffer.flip();
                    sc.write(buffer);
                    sc.register(selector, SelectionKey.OP_READ);
                } else if (key.isWritable()) {
                    continue;
                }

            }
        }

    }

    @Override
    public void run() {
        try {
            service();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
