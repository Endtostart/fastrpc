package netty;

import context.ApplicationContext;
import netty.base.SocketBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.ProviderService;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VirtualServer extends Thread{

    private ProviderService providerService = new ProviderService();
    private SocketBuffer socketBuffer = (SocketBuffer) ApplicationContext.get(SocketBuffer.class);
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
                    condition.signalAll();
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
            providerService.doProcess(socketBuffer.getServerStack());
        }finally {
            lock.unlock();
        }
    }


    public void accept() {
        this.start();
    }

    public void response(String message) {
        socketBuffer.setClientStack(message);
    }

}
