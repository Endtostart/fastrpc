package fastrpc.exception;

public class BeanNotFindException extends RuntimeException {
    private Throwable ex;
    public BeanNotFindException(String msg) {
        super(msg);
    }

    public BeanNotFindException(String msg, Throwable ex) {
        super(msg, ex);
        this.ex = ex;
    }

    public Throwable getException() {
        return this.ex;
    }

    @Override
    public synchronized Throwable getCause() {
        return this.ex;
    }

}
