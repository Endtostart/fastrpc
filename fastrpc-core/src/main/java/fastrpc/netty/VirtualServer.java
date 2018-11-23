package fastrpc.netty;

import fastrpc.context.ApplicationContext;
import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.Weave;
import fastrpc.exception.RpcCallbackException;
import fastrpc.netty.base.SocketBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fastrpc.rpc.ProviderService;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Bean
public class VirtualServer extends Thread{

    @Weave
    private ProviderService providerService;
    @Weave
    private SocketBuffer socketBuffer;

    Logger logger = LoggerFactory.getLogger(VirtualServer.class);
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    @Override
    public void run() {

        try {
            lock.lock();
            socketBuffer.addServerListener(() -> {
                try {
                    lock.lock();
                    logger.info("\n Server= = =>>" + Thread.currentThread().getName() + " : signal");
                    condition.signal();
                }finally {
                    lock.unlock();
                }

            });

            while (!socketBuffer.hasServerMsg()) {
                try {
                    logger.info("\n Server = = =>>" + Thread.currentThread().getName() + " : begain await");
                    condition.await();
                    logger.info("\n Server = = =>>" + Thread.currentThread().getName() + " : end await");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.info("\n Server = = =>>" + Thread.currentThread().getName() + " : out loop");
            try {
                providerService.doProcess(socketBuffer.getServerStack());
            } catch (RpcCallbackException ex) {
                logger.info("= = = do callback function = = = ");
                ex.doCallBack(this);
            }

        }finally {
            lock.unlock();
        }
    }


    public void accept() {
        this.start();
    }

    public void response(byte[] message) {
        socketBuffer.setClientStack(message);
    }

}
