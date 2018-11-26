package demo;

import fastrpc.context.annotation.RpcService;

@RpcService(name = "userService")
public class UserServiceImpl implements UserService{
    @Override
    public User getUserInfo(String id) {
        User user = new User();
        user.setId(111000);
        user.setName("trojan");
        user.setPassword("123456");
        return user;
    }

}
