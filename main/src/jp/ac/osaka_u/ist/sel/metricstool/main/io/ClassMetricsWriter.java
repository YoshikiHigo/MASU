package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;


public interface ClassMetricsWriter extends MetricsWriter {

    /**
     * �N���X���̃^�C�g��������
     */
    String CLASS_NAME = new String("\"Class Name\"");

    /**
     * �N���X���g���N�X��ۑ����Ă���萔
     */
    ClassMetricsInfoManager CLASS_METRICS_MANAGER = ClassMetricsInfoManager.getInstance();

}
