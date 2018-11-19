package netty;

import context.ApplicationContext;
import netty.base.SocketBuffer;
import rpc.ProviderService;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VirtualServer extends Thread{

    private ProviderService providerService = new ProviderService();
    private SocketBuffer socketBuffer = (SocketBuffer) ApplicationContext.get(SocketBuffer.class);
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    @Override
    public void run() {

        socketBuffer.addServerListener(()-> {
            condition.signal();
        });

        while (!socketBuffer.hasServerMsg()) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        response(socketBuffer.getServerStack());
    }


    public void accept() {
        this.start();
    }

    public void response(String message) {
        providerService.doProcess(message);
    }
}
