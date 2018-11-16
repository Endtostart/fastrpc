package message;

public interface IResponse<T> {

    public void setRequestId(String requestId);

    public void setValue(T value);

    public String getRequestId();

    public T getValue();

}
