package fastrpc.context;

import fastrpc.context.register.ServiceContent;

public interface ServiceApplicationAwake {
    public void setApplication(ServiceContent serviceContent);
}
