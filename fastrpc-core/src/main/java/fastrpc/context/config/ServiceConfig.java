package fastrpc.context.config;

/**
 * 服务提供方配置
 */
public class ServiceConfig {
    private String scanPath;
    private String port;

    public String getScanPath() {
        return scanPath;
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
