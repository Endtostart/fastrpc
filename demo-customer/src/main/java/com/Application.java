package com;

import com.free.UserService;
import fastrpc.context.BootStrap;
import fastrpc.context.CustomerContext;
import fastrpc.context.Strap;
import fastrpc.context.config.ApplicationConfig;
import fastrpc.context.config.CustomerConfig;
import fastrpc.context.support.ApplicationUtils;

public class Application {
    public static void main(String[] args) {
        CustomerConfig customerConfig = new CustomerConfig();
        customerConfig.setPort("8860");
        customerConfig.setRemoteIp("127.0.0.1");
        customerConfig.setScanPath("com.free");

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setCustomerConfig(customerConfig);
        ApplicationUtils.setApplicationConfig(applicationConfig);
        BootStrap bootStrap = new BootStrap(applicationConfig);
        Strap strap = bootStrap.strap();
        strap.start();
        System.out.println("already start customer");

        CustomerContext customerContext = bootStrap.getCustomerContext();
        UserService userService = customerContext.getBeanByName("userService");
        System.out.println(userService.getUserInfo());
    }
}
