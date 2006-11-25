package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * �N���X���g���N�X��o�^���邽�߂̃f�[�^�N���X
 * 
 * @author y-higo
 * 
 */
public final class ClassMetricsInfo {

    /**
     * �����Ȃ��R���X�g���N�^�D
     */
    public ClassMetricsInfo() {
        this.classMetrics = new ConcurrentHashMap<AbstractPlugin, Float>();
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
        if (this.classMetrics.containsKey(key)) {
            throw new MetricAlreadyRegisteredException();
        }

        this.classMetrics.put(key, value);
    }

    /**
     * �N���X���g���N�X��ۑ����邽�߂̕ϐ�
     */
    private final Map<AbstractPlugin, Float> classMetrics;
}

