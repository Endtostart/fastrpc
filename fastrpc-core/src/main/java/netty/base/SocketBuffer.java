package netty.base;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟socket缓冲区数据结构
 */
public class SocketBuffer {
    private String serverStack;

    private String clientStack;

    private Boolean hasServerMsg;

    private Boolean hasClientMsg;

    private List<Listener> serverListeners = new ArrayList<>();

    private List<Listener> clientListeners = new ArrayList<>();

    public String getServerStack() {
        return serverStack;
    }

    public void setServerStack(String serverStack) {
        this.serverStack = serverStack;
        trackServerAction();
    }

    public String getClientStack() {
        return clientStack;
    }

    public void setClientStack(String clientStack) {
        this.clientStack = clientStack;
        trackClientAction();
    }

    public Boolean hasServerMsg() {
        return !Strings.isNullOrEmpty(serverStack);
    }

    public Boolean hasClientMsg() {
        return !Strings.isNullOrEmpty(clientStack);
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
    }

    public void trackClientAction() {
        if (clientListeners == null || clientListeners.isEmpty()) {
            return;
        }
        clientListeners.forEach(e -> e.action());
    }
}
