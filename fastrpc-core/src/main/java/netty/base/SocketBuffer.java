package netty.base;

import com.google.common.base.Strings;

import java.util.List;

/**
 * 模拟socket缓冲区数据结构
 */
public class SocketBuffer {
    private String serverStack;

    private String clientStack;

    private Boolean hasServerMsg;

    private Boolean hasClientMsg;

    private List<Listener> serverListeners;

    private List<Listener> clientListeners;

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

    public Listener removerServerListener(Listener listener) {
        Listener temp = listener;
        serverListeners.remove(listener);
        return temp;
    }

    public void clearServerListener() {
        serverListeners.clear();
    }

    public void trackServerAction() {
        if (serverListeners == null || serverListeners.isEmpty()) {
            return;
        }
        serverListeners.forEach(e -> e.action());
    }
}
