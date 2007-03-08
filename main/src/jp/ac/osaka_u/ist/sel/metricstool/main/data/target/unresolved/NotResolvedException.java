package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;




public class NotResolvedException extends RuntimeException {

    public NotResolvedException() {
        super();
    }

    public NotResolvedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotResolvedException(String message) {
        super(message);
    }

    public NotResolvedException(Throwable cause) {
        super(cause);
    }
}
