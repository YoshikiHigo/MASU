package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


/**
 * ���ɓo�^���Ă��郁�g���N�X�����ēx�o�^���悤�Ƃ����ꍇ�ɁC�X���[������O
 * 
 * @author higo 
 */
public class MetricAlreadyRegisteredException extends Exception {

    public MetricAlreadyRegisteredException() {
        super();
    }

    public MetricAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricAlreadyRegisteredException(String message) {
        super(message);
    }

    public MetricAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

}
