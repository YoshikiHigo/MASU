package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

/**
 * ���p�\�łȂ��^�����o���ꂽ���� throw ������O
 * 
 * @author y-higo
 *
 */
public class UnknownTypeException extends Exception {
    public UnknownTypeException() {
        super();
    }

    public UnknownTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownTypeException(String message) {
        super(message);
    }

    public UnknownTypeException(Throwable cause) {
        super(cause);
    }
}
