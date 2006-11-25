package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;


/**
 * ���̃C���^�[�t�F�[�X�́C���\�b�h���g���N�X��o�^���邽�߂̃��\�b�h�Q��񋟂���D
 * 
 * @author y-higo
 *
 */
public interface MethodMetricsRegister {

    /**
     * �������̃��\�b�h�̃��g���N�X�l�i�������j��o�^����
     * 
     * @param methodInfo ���g���N�X�̌v���Ώۃ��\�b�h
     * @param value ���g���N�X�l
     * @throws �o�^���悤�Ƃ��Ă��郁�g���N�X�����ɓo�^����Ă���ꍇ�ɃX���[�����
     */
    void registMetric(MethodInfo methodInfo, int value) throws MetricAlreadyRegisteredException;
}
