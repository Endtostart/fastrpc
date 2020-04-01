package fastrpc.transport.jdknio;

import fastrpc.transport.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClient extends Thread implements Client {
    private final String host;
    private final int port;
    private SocketChannel channel;
    private InetSocketAddress inet;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(2048);


    private InheritableThreadLocal<byte[]> sendMsg = new InheritableThreadLocal();
    private InheritableThreadLocal<byte[]> resMsg = new InheritableThreadLocal();

    private byte[] sendByte;
    private byte[] resByte;

    Logger logger = LoggerFactory.getLogger(NioClient.class);

    public NioClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public NioClient addThreadLocal(InheritableThreadLocal<byte[]> sendMsg,InheritableThreadLocal<byte[]> resMsg) {
        this.sendMsg = sendMsg;
        this.resMsg = resMsg;
        return this;
    }


    public NioClient init() {
        inet = new InetSocketAddress(host, port);
        try {
            channel = SocketChannel.open();
            channel.socket().connect(inet);
            channel.configureBlocking(false);
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.start();
        return this;
    }

    @Override
    public void send() {
        /*byte[] bytes = sendMsg.get();*/
        byte[] bytes = sendByte;
        buffer.clear();
        buffer.put(bytes);
        //buffer.get(bytes);
        try {
            buffer.flip();
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_WRITE | SelectionKey.OP_READ);

            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iKeys = keys.iterator();
                while (iKeys.hasNext()) {
                    SelectionKey key = iKeys.next();
                    iKeys.remove();
                    SocketChannel sc = (SocketChannel) key.channel();
                    if (key.isConnectable()) {
                        if (sc.isConnectionPending()) {
                            if (sc.finishConnect()) {
                                sc.configureBlocking(false);
                                sc.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                                logger.info("= = = 客户端连接成功 = = = \n 连接地址：" + inet.getHostName() + ": " + port);
                            }
                        }
                    } else if (key.isReadable()) {
                        buffer.clear();
                        sc.read(buffer);
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes, 0, buffer.remaining());

                        resByte = bytes;
                        /*resMsg.set(bytes);*/
                    } else if (key.isWritable()) {
                        /*if (sendMsg.get() != null) {
                            send();
                        }*/
                        if (sendByte != null && sendByte.length > 0) {
                            send();
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InheritableThreadLocal<byte[]> getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(InheritableThreadLocal<byte[]> sendMsg) {
        this.sendMsg = sendMsg;
    }

    public InheritableThreadLocal<byte[]> getResMsg() {
        return resMsg;
    }

    public void setResMsg(InheritableThreadLocal<byte[]> resMsg) {
        this.resMsg = resMsg;
    }

    public byte[] getSendByte() {
        return sendByte;
    }

    public void setSendByte(byte[] sendByte) {
        this.sendByte = sendByte;
    }

    public byte[] getResByte() {
        return resByte;
    }

    public void setResByte(byte[] resByte) {
        this.resByte = resByte;
    }
}
