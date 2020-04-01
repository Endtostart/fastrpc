package com.free;

import fastrpc.context.annotation.RpcConsume;

@RpcConsume
public interface UserService {
    User getUserInfo();
}
