package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * �ڑ����ؒf���ꂽ�ꍇ�ɔ�������
 * 
 * @author kou-tngt
 *
 */
public class DisconnectedException extends ConnectionException {

    public DisconnectedException() {
        super();
    }

    public DisconnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisconnectedException(String message) {
        super(message);
    }

    public DisconnectedException(Throwable cause) {
        super(cause);
    }

}
