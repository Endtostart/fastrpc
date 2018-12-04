package com;

import fastrpc.context.BootStrap;
import fastrpc.context.Strap;
import fastrpc.context.config.ApplicationConfig;
import fastrpc.context.config.CustomerConfig;
import fastrpc.context.support.ApplicationUtils;

public class Application {
    public static void main(String[] args) {
        CustomerConfig customerConfig = new CustomerConfig();
        customerConfig.setPort("8860");
        customerConfig.setRemoteIp("127.0.0.1");
        customerConfig.setScanPath("free.com");

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setCustomerConfig(customerConfig);
        ApplicationUtils.setApplicationConfig(applicationConfig);
        BootStrap bootStrap = new BootStrap(applicationConfig);
        Strap strap = bootStrap.strap();
        strap.start();


    }
}
