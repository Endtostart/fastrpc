package fastrpc.context;

import fastrpc.context.annotation.Bean;
import fastrpc.context.annotation.RpcService;
import fastrpc.context.annotation.Weave;

/**
 * @Author N.wang
 * @Date 13:51 2018/11/23
 * @Desc 注解枚举
 **/
public enum AnnotationEnum {
    BEAN(Bean.class),
    DEPANDENCE(Weave.class),
    SERVICE(RpcService.class);

    private Class classType;

    private AnnotationEnum(Class classType) {
        this.classType = classType;
    }

    public Class getClassType() {
        return classType;
    }
}
