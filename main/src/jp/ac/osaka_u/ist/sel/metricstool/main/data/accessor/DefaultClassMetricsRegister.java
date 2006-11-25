package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE;


/**
 * �v���O�C�����N���X���g���N�X��o�^���邽�߂ɗp����N���X�D
 * 
 * @author y-higo
 */
public class DefaultClassMetricsRegister implements ClassMetricsRegister {

    /**
     * �o�^�����p�̃I�u�W�F�N�g�̏��������s���D�v���O�C���͎��g�������Ƃ��ė^���Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param plugin ���������s���v���O�C���̃C���X�^���X
     */
    public DefaultClassMetricsRegister(final AbstractPlugin plugin) {

        if (null == plugin) {
            throw new NullPointerException();
        }
        PluginInfo pluginInfo = plugin.getPluginInfo();
        if (METRICS_TYPE.CLASS_METRICS != pluginInfo.getMetricsType()) {
            throw new IllegalArgumentException("plugin must be class type!");
        }

        this.plugin = plugin;
    }

    /**
     * �������̃N���X�̃��g���N�X�l�i�������j��o�^����
     */
    public void registMetric(final ClassInfo classInfo, final int value)
            throws MetricAlreadyRegisteredException {
        ClassMetricsInfoManager manager = ClassMetricsInfoManager.getInstance();
        manager.putMetric(classInfo, this.plugin, value);
    }

    /**
     * �v���O�C���I�u�W�F�N�g��ۑ����Ă������߂̕ϐ�
     */
    private final AbstractPlugin plugin;
}
