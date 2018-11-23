package fastrpc.netty.base;

import fastrpc.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 模拟socket缓冲区数据结构
 */
@Bean
public class SocketBuffer {
    private byte[] serverStack;

    private byte[] clientStack;

    private Boolean hasServerMsg;

    private Boolean hasClientMsg;

    private List<Listener> serverListeners = new ArrayList<>();

    private List<Listener> clientListeners = new ArrayList<>();

    public byte[] getServerStack() {
        return serverStack;
    }

    public void setServerStack(byte[] serverStack) {
        this.serverStack = serverStack;
        trackServerAction();
    }

    public byte[] getClientStack() {
        return clientStack;
    }

    public void setClientStack(byte[] clientStack) {
        this.clientStack = clientStack;
        trackClientAction();
    }

    public Boolean hasServerMsg() {
        return !(Objects.isNull(serverStack) || serverStack.length == 0);
    }

    public Boolean hasClientMsg() {
        return !(Objects.isNull(clientStack) || clientStack.length == 0);
    }

    public void addServerListener(Listener listener) {
        if (!serverListeners.contains(listener)) {
            serverListeners.add(listener);
        }
    }

    public Listener removeServerListener(Listener listener) {
        serverListeners.remove(listener);
        return listener;
    }

    public void addClientListener(Listener listener) {
        clientListeners.add(listener);
    }

    public Listener removeClientListener(Listener listener) {
        clientListeners.remove(listener);
        return listener;
    }

    public void clearServerListener() {
        serverListeners.clear();
    }

    public void clearClientListener() {
        clientListeners.clear();
    }

    public void trackServerAction() {
        if (serverListeners == null || serverListeners.isEmpty()) {
            return;
        }
        serverListeners.forEach(e -> e.action());
        clearClientListener();
    }

    public void trackClientAction() {
        if (clientListeners == null || clientListeners.isEmpty()) {
            return;
        }
        clientListeners.forEach(e -> e.action());
        clearServerListener();
    }
}
