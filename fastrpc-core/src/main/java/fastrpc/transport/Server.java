package fastrpc.transport;

import java.io.IOException;

public interface Server {
    void service() throws IOException;
}
