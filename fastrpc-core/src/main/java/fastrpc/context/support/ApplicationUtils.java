package fastrpc.context.support;

import fastrpc.context.RpcBeanContent;
import fastrpc.context.config.ApplicationConfig;

public class ApplicationUtils {
    private static RpcBeanContent beanContent;
    private static ApplicationConfig applicationConfig;

    private ApplicationUtils() {
    }

    public static void setBeanContent(RpcBeanContent beanContent) {
        ApplicationUtils.beanContent = beanContent;
    }

    public static RpcBeanContent getBeanContent() {
        return ApplicationUtils.beanContent;
    }

    private static ApplicationConfig getApplicationConfig() {
        return ApplicationUtils.applicationConfig;
    }

    public static int getServerPort() {
        return Integer.parseInt(applicationConfig.getCustomerConfig().getPort());
    }

    public static String getServiceHost() {
        return applicationConfig.getCustomerConfig().getRemoteIp();
    }

    public static void setApplicationConfig(ApplicationConfig applicationConfig) {
        ApplicationUtils.applicationConfig = applicationConfig;
    }
}
