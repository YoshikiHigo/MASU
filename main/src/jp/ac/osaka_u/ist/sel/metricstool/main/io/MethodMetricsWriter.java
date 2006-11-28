package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;


public interface MethodMetricsWriter extends MetricsWriter {

    /**
     * ���\�b�h���̃^�C�g��������
     */
    String METHOD_NAME = new String("\"Method Name\"");

    /**
     * ���\�b�h���g���N�X��ۑ����Ă���萔
     */
    MethodMetricsInfoManager METHOD_METRICS_MANAGER = MethodMetricsInfoManager.getInstance();

}
