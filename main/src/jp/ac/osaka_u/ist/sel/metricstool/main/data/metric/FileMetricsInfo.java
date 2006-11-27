package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;


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
    public FileMetricsInfo(final FileInfo fileInfo) {

        if (null == fileInfo) {
            throw new NullPointerException();
        }

        this.fileInfo = fileInfo;
        this.fileMetrics = Collections.synchronizedSortedMap(new TreeMap<AbstractPlugin, Float>());
    }

    /**
     * ���̃��g���N�X���̃t�@�C����Ԃ�
     * 
     * @return ���̃��g���N�X���̃t�@�C��
     */
    public FileInfo getFileInfo() {
        return this.fileInfo;
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
     * ���̃��g���N�X���ɕs�����Ȃ������`�F�b�N����
     * 
     * @throws MetricNotRegisteredException
     */
    void checkMetrics() throws MetricNotRegisteredException {
        PluginManager pluginManager = PluginManager.getInstance();
        for (AbstractPlugin plugin : pluginManager.getPlugins()) {
            Float value = this.getMetric(plugin);
            if (null == value) {
                PluginInfo pluginInfo = plugin.getPluginInfo();
                String metricName = pluginInfo.getMetricName();
                FileInfo fileInfo = this.getFileInfo();
                String fileName = fileInfo.getName();
                throw new MetricNotRegisteredException("Metric \"" + metricName + "\" of "
                        + fileName + " is not registered!");
            }
        }
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
     * ���̃��g���N�X���̃t�@�C����ۑ����邽�߂̕ϐ�
     */
    private final FileInfo fileInfo;

    /**
     * �t�@�C�����g���N�X��ۑ����邽�߂̕ϐ�
     */
    private final SortedMap<AbstractPlugin, Float> fileMetrics;
}
