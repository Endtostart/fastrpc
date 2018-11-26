package fastrpc.exception;

public class ParameterValidException extends RuntimeException{
    private Throwable ex;

    public ParameterValidException(String msg) {
        super(msg);
    }

    public ParameterValidException(String msg, Throwable ex) {
        super(msg, ex);
        this.ex = ex;
    }

    @Override
    public synchronized Throwable getCause() {
        return this.ex;
    }

    public Throwable getException() {
        return this.ex;
    }

}
