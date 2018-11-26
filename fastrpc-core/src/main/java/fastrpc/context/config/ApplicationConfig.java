package fastrpc.context.config;

public class ApplicationConfig {
    private ServiceConfig serviceConfig;
    private CustomerConfig customerConfig;

    public String getServicePath() {
        return this.serviceConfig.getScanPath();
    }

    public String getCustomerPath(){
        return this.customerConfig.getScanPath();
    }

    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public CustomerConfig getCustomerConfig() {
        return customerConfig;
    }

    public void setCustomerConfig(CustomerConfig customerConfig) {
        this.customerConfig = customerConfig;
    }
}
