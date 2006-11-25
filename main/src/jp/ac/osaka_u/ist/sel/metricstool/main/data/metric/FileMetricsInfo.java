package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * �t�@�C�����g���N�X��o�^���邽�߂̃f�[�^�N���X
 * 
 * @author y-higo
 * 
 */
public final class FileMetricsInfo {

    /**
     * �����Ȃ��R���X�g���N�^�D
     */
    public FileMetricsInfo() {
        this.fileMetrics = new ConcurrentHashMap<AbstractPlugin, Float>();
    }
    
    /**
     * �����Ŏw�肵���v���O�C���ɂ���ēo�^���ꂽ���g���N�X�����擾���郁�\�b�h�D
     * 
     * @param key �ق������g���N�X��o�^�����v���O�C��
     * @return ���g���N�X�l
     * @throws MetricNotRegisteredException ���g���N�X���o�^����Ă��Ȃ��Ƃ��ɃX���[�����
     */
    public float getMetric(final AbstractPlugin key) throws MetricNotRegisteredException {

        if (null == key) {
            throw new NullPointerException();
        }

        Float value = this.fileMetrics.get(key);
        if (null == value) {
            throw new MetricNotRegisteredException();
        }

        return value.floatValue();
    }
    
    /**
     * �������ŗ^����ꂽ�v���O�C���Ōv�����ꂽ���g���N�X�l�i�������j��o�^����D
     * 
     * @param key �v�������v���O�C���C���X�^���X�CMap �̃L�[�Ƃ��ėp����D
     * @param value �o�^���郁�g���N�X�l(int)
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă������g���N�X�����ɓo�^����Ă����ꍇ�ɃX���[�����
     */
    public void putMetric(final AbstractPlugin key, final int value)
            throws MetricAlreadyRegisteredException {
        this.putMetric(key, new Float(value));
    }

    /**
     * �������ŗ^����ꂽ�v���O�C���Ōv�����ꂽ���g���N�X�l�i�������j��o�^����D
     * 
     * @param key �v�������v���O�C���C���X�^���X�CMap �̃L�[�Ƃ��ėp����D
     * @param value �o�^���郁�g���N�X�l(float)
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă������g���N�X�����ɓo�^����Ă����ꍇ�ɃX���[�����
     */
    public void putMetric(final AbstractPlugin key, final float value)
            throws MetricAlreadyRegisteredException {
        this.putMetric(key, new Float(value));
    }

    /**
     * �������ŗ^����ꂽ�v���O�C���Ōv�����ꂽ���g���N�X�l�i�������j��o�^����D
     * 
     * @param key �v�������v���O�C���C���X�^���X�CMap �̃L�[�Ƃ��ėp����D
     * @param value �o�^���郁�g���N�X�l
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă������g���N�X�����ɓo�^����Ă����ꍇ�ɃX���[�����
     */
    private void putMetric(final AbstractPlugin key, final Float value)
            throws MetricAlreadyRegisteredException {

        if (null == key) {
            throw new NullPointerException();
        }
        if (this.fileMetrics.containsKey(key)) {
            throw new MetricAlreadyRegisteredException();
        }

        this.fileMetrics.put(key, value);
    }

    /**
     * �t�@�C�����g���N�X��ۑ����邽�߂̕ϐ�
     */
    private final Map<AbstractPlugin, Float> fileMetrics;
}
