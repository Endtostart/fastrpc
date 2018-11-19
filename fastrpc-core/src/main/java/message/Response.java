package message;

import java.io.Serializable;

public class Response<T> implements Serializable,IResponse<T>{

    private static final long serialVersionUID = 1168814620391610219L;

    private String requestId;

    private T value;

    public Response() {

    }

    public Response(T value) {
        this.value = value;
    }

    public Response(String requestId, T value) {
        this.requestId = requestId;
        this.value = value;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public T getValue() {
        return value;
    }

}
