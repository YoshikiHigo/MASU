package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * ���O��������Ă��Ȃ����𗘗p���悤�Ƃ������ɃX���[������O
 * 
 * @author higo
 *
 */
public class NotResolvedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5769149366891479545L;

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
